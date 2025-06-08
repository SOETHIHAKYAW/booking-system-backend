package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.UserPackageDTO;
import org.demo.bookingsystem.entity.Package;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.entity.UserPackage;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class UserPackageMapper {

    public UserPackageDTO toDTO(UserPackage entity) {
        if (entity == null) return null;
        UserPackageDTO dto = new UserPackageDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setPackageId(entity.getPack() != null ? entity.getPack().getId() : null);
        dto.setCountryCode(entity.getPack() != null ? entity.getPack().getCountryCode() : null);
        dto.setRemainingCredits(entity.getRemainingCredits());
        dto.setExpirationDate(entity.getExpirationDate());

        Boolean isExpired = entity.getExpirationDate() != null
                && entity.getExpirationDate().isBefore(LocalDateTime.now());
        dto.setExpired(isExpired);

        return dto;
    }

    public UserPackage toEntity(UserPackageDTO dto, User user, Package pack) {
        if (dto == null) return null;
        UserPackage entity = new UserPackage();
        entity.setId(dto.getId());
        entity.setUser(user);
        entity.setPack(pack);
        entity.setRemainingCredits(dto.getRemainingCredits());
        entity.setExpirationDate(dto.getExpirationDate());
        entity.setCountryCode(dto.getCountryCode() != null ? dto.getCountryCode() : pack.getCountryCode());

        Boolean isExpired = dto.getExpirationDate() != null
                && dto.getExpirationDate().isBefore(LocalDateTime.now());
        entity.setExpired(isExpired);
        return entity;
    }
}