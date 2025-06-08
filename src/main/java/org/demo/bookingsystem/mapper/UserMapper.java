package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.UserDTO;
import org.demo.bookingsystem.dto.UserProfileResponseDTO;
import org.demo.bookingsystem.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class UserMapper {

    public static UserDTO mapToDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setCountryCode(user.getCountryCode());
        dto.setEmail(user.getEmail());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setVerificationCode(user.getVerificationCode());
        dto.setActive(user.getActive());
        dto.setRoleName(user.getRole() != null ? user.getRole().getName().toString() : null);
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setDeletedAt(user.getDeletedAt());

        return dto;
    }

    public static void updateEntity(UserDTO dto, User user) {
        if (dto == null || user == null) return;

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setEmailVerified(dto.getEmailVerified());
        user.setActive(dto.getActive());
        user.setCountryCode(dto.getCountryCode());
    }

    public static UserProfileResponseDTO toUserProfileDTO(User user) {
        if (user == null) return null;

        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEmailVerified(user.getEmailVerified());
        dto.setActive(user.getActive());
        dto.setCountryCode(user.getCountryCode());
        dto.setRoleName(user.getRole() != null ? String.valueOf(user.getRole().getName()) : null);
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        List<UserProfileResponseDTO.UserPackageDTO> packages = user.getPurchasedPackages() != null
                ? user.getPurchasedPackages().stream()
                .filter(p -> p != null && p.getPack() != null)
                .map(p -> {
                    UserProfileResponseDTO.UserPackageDTO pkgDto = new UserProfileResponseDTO.UserPackageDTO();
                    pkgDto.setId(p.getId());
                    pkgDto.setPackageName(p.getPack().getName());         // fixed
                    pkgDto.setTotalCredits(p.getPack().getCredits());     // fixed
                    pkgDto.setRemainingCredits(p.getRemainingCredits());
                    // PurchasedAt field — not available, consider adding it to UserPackage if needed
                    pkgDto.setPurchasedAt(null);
                    pkgDto.setExpiredAt(p.getExpirationDate());
                    pkgDto.setExpired(p.getExpirationDate() != null && p.getExpirationDate().isBefore(LocalDateTime.now()));
                    return pkgDto;
                }).toList()
                : List.of();

        dto.setPurchasedPackages(packages);

        // Credit summary
        int totalCredits = packages.stream().mapToInt(UserProfileResponseDTO.UserPackageDTO::getTotalCredits).sum();
        int remainingCredits = packages.stream().mapToInt(UserProfileResponseDTO.UserPackageDTO::getRemainingCredits).sum();

        dto.setTotalCredits(totalCredits);
        dto.setRemainingCredits(remainingCredits);

        return dto;
    }
}