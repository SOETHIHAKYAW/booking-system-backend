package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.demo.bookingsystem.dto.UserPackageDTO;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.UserPackageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * UserPackageController handles all CRUD operations related to user packages.
 */
@RestController
@RequestMapping("api/v1/user-packages")
@Tag(name = "User Package Management", description = "Operations related to user packages")
public class UserPackageController {

    private final UserPackageService userPackageService;

    public UserPackageController(UserPackageService userPackageService) {
        this.userPackageService = userPackageService;
    }

    /**
     * Create a new user package.
     *
     * @param dto The UserPackageDTO to be created.
     * @return ResponseEntity containing the created UserPackageDTO.
     */
    @PostMapping
    @Operation(summary = "Create a user package", description = "Add a new user package")
    public ResponseEntity<ApiResponse<UserPackageDTO>> create(@RequestBody UserPackageDTO dto) {
        UserPackageDTO created = userPackageService.create(dto);
        ApiResponse<UserPackageDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "User package created", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all user packages with pagination.
     */
    @GetMapping
    @Operation(summary = "Get all user packages", description = "Fetch all user packages with pagination")
    public ResponseEntity<ApiResponse<Page<UserPackageDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserPackageDTO> pageable = userPackageService.getAll(PageRequest.of(page, size));
        ApiResponse<Page<UserPackageDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "User packages fetched", pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a user package by its ID.
     *
     * @param id UUID of the user package.
     * @return ResponseEntity containing the UserPackageDTO if found.
     */
    @GetMapping("/id/{id}")
    @Operation(summary = "Get a user package by ID", description = "Fetch a user package by its UUID")
    public ResponseEntity<ApiResponse<UserPackageDTO>> getById(@PathVariable UUID id) {
        Optional<UserPackageDTO> optional = userPackageService.getById(id);
        if (optional.isPresent()) {
            ApiResponse<UserPackageDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "User package found", optional.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserPackageDTO> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User package not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Update a user package.
     *
     * @param id  UUID of the user package to update.
     * @param dto The updated UserPackageDTO.
     * @return ResponseEntity containing the updated UserPackageDTO.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user package", description = "Update an existing user package by its UUID")
    public ResponseEntity<ApiResponse<UserPackageDTO>> update(@PathVariable UUID id, @RequestBody UserPackageDTO dto) {
        UserPackageDTO updated = userPackageService.update(id, dto);
        ApiResponse<UserPackageDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "User package updated", updated);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a user package by ID.
     *
     * @param id UUID of the user package to delete.
     * @return ResponseEntity with HTTP 204 No Content.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user package", description = "Delete a user package by its UUID")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        userPackageService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "User package deleted", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    /**
     * Purchase a package for a user.
     *
     * @param userId    UUID of the user buying the package.
     * @param packageId UUID of the package to purchase.
     * @return ResponseEntity containing the purchased UserPackageDTO.
     */
    @PostMapping("/purchase")
    @Operation(summary = "Purchase a package", description = "User purchases a package by userId and packageId")
    public ResponseEntity<ApiResponse<UserPackageDTO>> purchasePackage(
            @RequestParam UUID userId,
            @RequestParam UUID packageId) {

        UserPackageDTO purchased = userPackageService.purchasePackage(userId, packageId);
        ApiResponse<UserPackageDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Package purchased successfully", purchased);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}