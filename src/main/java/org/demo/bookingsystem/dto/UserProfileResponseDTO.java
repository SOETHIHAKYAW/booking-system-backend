package org.demo.bookingsystem.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for returning user profile information including purchased packages and credit info.
 */
@Data
public class UserProfileResponseDTO {

    private UUID id;
    private String username;
    private String email;
    private Boolean emailVerified;
    private Boolean active;
    private String countryCode;
    private String roleName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<UserPackageDTO> purchasedPackages;

    private int totalCredits;
    private int remainingCredits;

    @Data
    public static class UserPackageDTO {
        private UUID id;
        private String packageName;
        private int totalCredits;
        private int remainingCredits;
        private LocalDateTime purchasedAt;
        private LocalDateTime expiredAt;
        private boolean expired;
    }
}