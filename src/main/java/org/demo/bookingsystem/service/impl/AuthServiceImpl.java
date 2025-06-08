package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.SignInDTO;
import org.demo.bookingsystem.dto.SignUpDTO;
import org.demo.bookingsystem.dto.UserDTO;
import org.demo.bookingsystem.entity.Role;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.enums.RoleName;
import org.demo.bookingsystem.exception.ResourceNotFoundException;
import org.demo.bookingsystem.mapper.UserMapper;
import org.demo.bookingsystem.repository.RoleRepository;
import org.demo.bookingsystem.repository.UserRepository;
import org.demo.bookingsystem.service.AuthService;
import org.demo.bookingsystem.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDTO signup(SignUpDTO signUpDTO) {
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        User user = new User();
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        user.setActive(false); // Default to inactive, user will activate via email verification
        user.setCountryCode(signUpDTO.getCountryCode());

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'ROLE_USER' not found"));

        user.setRole(defaultRole);

        // Generate short verification code like "BS_123456" - BS for Booking System
        Random random = new Random();
        int codeNumber = 100000 + random.nextInt(900000);
        String verificationCode = "BS_" + codeNumber;
        user.setVerificationCode(verificationCode);
        user.setEmailVerified(false);
        userRepository.save(user);

        boolean emailSent = emailService.sendVerifyEmail(user.getEmail(), verificationCode);
        if (!emailSent) {
            throw new RuntimeException("Failed to send verification email");
        }

        return UserMapper.mapToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO signIn(SignInDTO signInDTO) {
        User user = userRepository.findByUsername(signInDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(signInDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        if (!user.getEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email before signing in.");
        }

        return UserMapper.mapToDTO(user);
    }

    @Override
    public void verifyEmail(String code) {
        User user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid verification code"));

        user.setActive(true);
        user.setEmailVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
    }
}