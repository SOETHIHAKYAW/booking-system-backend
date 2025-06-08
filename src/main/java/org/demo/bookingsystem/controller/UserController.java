package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.demo.bookingsystem.dto.ChangePasswordRequestDTO;
import org.demo.bookingsystem.dto.ResetPasswordRequestDTO;
import org.demo.bookingsystem.dto.UserDTO;
import org.demo.bookingsystem.dto.UserProfileResponseDTO;
import org.demo.bookingsystem.repository.UserRepository;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/**
 * UserController handles user-related operations such as creating, updating, retrieving, deleting,
 * and managing profile/password.
 */
@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User Management", description = "Operations related to user management and profile")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // ========== Basic User CRUD ==========

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a user with password and role.")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
            @RequestBody UserDTO userDTO,
            @RequestParam String rawPassword,
            @RequestParam String roleName) {

        UserDTO savedUserDTO = userService.createUser(userDTO, rawPassword, roleName);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "User created successfully", savedUserDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Update user info and optionally role.")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable String id,
            @RequestBody UserDTO userDTO,
            @RequestParam(required = false) String roleName) {

        UUID userId = UUID.fromString(id);
        UserDTO updatedUserDTO = userService.updateUser(userId, userDTO, roleName);
        return ResponseEntity.ok(new ApiResponse<>(200, "User updated successfully", updatedUserDTO));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by ID", description = "Fetch a user from the database using their ID.")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse<>(200, "User found", userDTO));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users from the database with pagination.")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDTO> usersPage = userService.getAllUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(new ApiResponse<>(200, "Users retrieved", usersPage));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate a user by ID", description = "Deactivate a user by setting active = false.")
    public ResponseEntity<ApiResponse<Void>> deactivateUserById(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        userService.deactivateUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(204, "User deactivated successfully", null));
    }

    // ========== Profile & Password APIs ==========

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile", description = "Returns the profile of the currently logged-in user.")
    public ResponseEntity<ApiResponse<UserProfileResponseDTO>> getProfile(Principal principal) {

        UUID userId = getCurrentUserId(principal);

        UserProfileResponseDTO userProfile = userService.getUserProfile(userId);

        return ResponseEntity.ok(new ApiResponse<>(200, "Profile retrieved", userProfile));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password", description = "Allows the logged-in user to change their password.")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Principal principal,
            @Valid @RequestBody ChangePasswordRequestDTO request) {

        UUID userId = getCurrentUserId(principal);

        userService.changePassword(userId, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>(200, "Password changed successfully", null));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Trigger password reset using email/token.")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        userService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Password reset process started", null));
    }

    private UUID getCurrentUserId(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

}