package org.demo.bookingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for User entity
 * Does NOT include password for security reasons
 */
@Data
public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private Boolean emailVerified;
    private Boolean active;
    private String countryCode; // ISO country code, e.g. "SG", "MM"
    private String roleName;
    private String verificationCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
