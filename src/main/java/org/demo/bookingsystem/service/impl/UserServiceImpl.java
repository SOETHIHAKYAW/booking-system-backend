package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.ResetPasswordRequestDTO;
import org.demo.bookingsystem.dto.UserDTO;
import org.demo.bookingsystem.dto.UserProfileResponseDTO;
import org.demo.bookingsystem.entity.Role;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.enums.RoleName;
import org.demo.bookingsystem.exception.UserNotFoundException;
import org.demo.bookingsystem.mapper.UserMapper;
import org.demo.bookingsystem.repository.RoleRepository;
import org.demo.bookingsystem.repository.UserRepository;
import org.demo.bookingsystem.service.EmailService;
import org.demo.bookingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of UserService with pagination and role management
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::mapToDTO);
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.mapToDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileResponseDTO getUserProfile(UUID id) {
        User user = userRepository.findByIdWithPackages(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.toUserProfileDTO(user);
    }


    @Override
    public UserDTO createUser(UserDTO userDTO, String rawPassword, String roleName) {
        User user = new User();

        // Map basic fields from DTO
        UserMapper.updateEntity(userDTO, user);

        // Encode and set password
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Set role
        Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        user.setRole(role);

        // Save user
        User savedUser = userRepository.save(user);

        return UserMapper.mapToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO, String roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update fields from DTO
        UserMapper.updateEntity(userDTO, user);

        // Update role if provided
        if (roleName != null && !roleName.isBlank()) {
            Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            user.setRole(role);
        }

        User updatedUser = userRepository.save(user);

        return UserMapper.mapToDTO(updatedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        // Soft delete by setting active to false and deletedAt timestamp
        user.setActive(Boolean.FALSE);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setActive(false); // Soft delete by setting active to false
        userRepository.save(user);
    }

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(ResetPasswordRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        boolean emailSent = emailService.sendNewTempPassword(user.getEmail(), tempPassword);
        if (!emailSent) {
            throw new RuntimeException("Failed to send reset password email");
        }
    }
}