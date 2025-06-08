package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.ResetPasswordRequestDTO;
import org.demo.bookingsystem.dto.UserDTO;
import org.demo.bookingsystem.dto.UserProfileResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for managing User entities
 */
public interface UserService {

    /**
     * Get paginated list of users
     * @param pageable Pagination parameters
     * @return Page of UserDTO
     */
    Page<UserDTO> getAllUsers(Pageable pageable);

    /**
     * Get single user by id
     */
    UserDTO getUserById(UUID id);

    /**
     * Create a new user (password & role to be handled in implementation)
     */
    UserDTO createUser(UserDTO userDTO, String rawPassword, String roleName);

    /**
     * Update an existing user by id
     */
    UserDTO updateUser(UUID id, UserDTO userDTO, String roleName);

    /**
     * Delete user by id
     */
    void deleteUser(UUID id);

    /**
     * Deactivate user by id (soft delete)
     */
    void deactivateUser(UUID id);

    /**
     * Change user password
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password to set
     */
    void changePassword(UUID userId, String oldPassword, String newPassword);

    /**
     * Reset user password
     * @param request ResetPasswordRequestDTO containing user email and new password
     */
    void resetPassword(ResetPasswordRequestDTO request);

    /**
     * Get user profile information
     * @param id User ID
     * @return UserProfileResponseDTO containing user profile details
     */
    public UserProfileResponseDTO getUserProfile(UUID id);

}