package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.UserPackageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserPackageService {
    Page<UserPackageDTO> getAll(Pageable pageable);

    Optional<UserPackageDTO> getById(UUID id);

    UserPackageDTO create(UserPackageDTO dto);

    UserPackageDTO update(UUID id, UserPackageDTO dto);

    void delete(UUID id);

    UserPackageDTO purchasePackage(UUID userId, UUID packageId);

    boolean deductCredits(UUID userId, UUID packageId, int creditsToDeduct);

    void refundCredits(UUID userId, UUID packageId, int creditsToRefund);
}