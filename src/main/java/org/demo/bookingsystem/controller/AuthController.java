package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.demo.bookingsystem.config.JwtUtil;
import org.demo.bookingsystem.dto.SignInDTO;
import org.demo.bookingsystem.dto.SignUpDTO;
import org.demo.bookingsystem.dto.UserDTO;
import org.demo.bookingsystem.dto.UserIdResponseDTO;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.exception.ResourceNotFoundException;
import org.demo.bookingsystem.repository.UserRepository;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication Management", description = "Operations related to user authentication")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          AuthService authService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a new user", description = "Register a new user.")
    public ResponseEntity<ApiResponse<UserIdResponseDTO>> signup(@Valid @RequestBody SignUpDTO signUpDTO) {
        logger.info("Signup request received for username: {}", signUpDTO.getUsername());

        if (signUpDTO.getPassword() == null || signUpDTO.getPassword().isEmpty()) {
            logger.warn("Empty password submitted during signup.");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Password cannot be empty", null));
        }

        try {
            UserDTO savedUser = authService.signup(signUpDTO);
            logger.info("User '{}' registered successfully", savedUser.getUsername());

            UserIdResponseDTO userIdResponse = new UserIdResponseDTO(savedUser.getId());
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "User registered successfully", userIdResponse));

        } catch (RuntimeException e) {
            logger.error("Signup failed for user '{}': {}", signUpDTO.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Unexpected error during signup for '{}'", signUpDTO.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error during registration", null));
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Log in user and return JWT token")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody SignInDTO signInDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDTO.getUsername(), signInDTO.getPassword())
            );

            User user = userRepository.findByUsername(signInDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!user.getEmailVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>(HttpStatus.FORBIDDEN.value(), "Email not verified. Please verify your email.", null));
            }

            String token = jwtUtil.generateToken(signInDTO.getUsername());

            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Login successful", token));

        } catch (Exception e) {
            logger.warn("Login failed for user '{}': {}", signInDTO.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password", null));
        }
    }

    @GetMapping("/verify")
    @Operation(summary = "Verify user email", description = "Verify email using verification code")
    public ResponseEntity<ApiResponse<String>> verifyEmail(@RequestParam("code") String code) {
        try {
            authService.verifyEmail(code);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Email verified successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error verifying email", null));
        }
    }
}