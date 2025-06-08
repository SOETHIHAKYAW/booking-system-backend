package org.demo.bookingsystem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.demo.bookingsystem.dto.UserPackageDTO;
import org.demo.bookingsystem.entity.Package;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.entity.UserPackage;
import org.demo.bookingsystem.mapper.UserPackageMapper;
import org.demo.bookingsystem.repository.PackageRepository;
import org.demo.bookingsystem.repository.UserPackageRepository;
import org.demo.bookingsystem.repository.UserRepository;
import org.demo.bookingsystem.service.UserPackageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPackageServiceImpl implements UserPackageService {

    private final UserPackageRepository userPackageRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final UserPackageMapper mapper;

    @Override
    public Page<UserPackageDTO> getAll(Pageable pageable) {
        // Retrieve paged UserPackage entities and map to DTOs
        return userPackageRepository.findAllNotDeleted(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Optional<UserPackageDTO> getById(UUID id) {
        // Find UserPackage by ID and convert to DTO
        return userPackageRepository.findById(id)
                .map(mapper::toDTO);
    }

    @Override
    public UserPackageDTO create(UserPackageDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Package pack = packageRepository.findById(dto.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        UserPackage entity = buildUserPackage(user, pack, dto.getRemainingCredits(), dto.getExpirationDate());
        UserPackage saved = userPackageRepository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public UserPackageDTO update(UUID id, UserPackageDTO dto) {
        // Find existing UserPackage
        UserPackage existing = userPackageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UserPackage not found"));

        // Validate user and package
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Package pack = packageRepository.findById(dto.getPackageId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        // Update fields
        existing.setUser(user);
        existing.setPack(pack);
        existing.setRemainingCredits(dto.getRemainingCredits());
        existing.setExpirationDate(dto.getExpirationDate());

        // Calculate expired dynamically based on expirationDate and today
        boolean isExpired = existing.getExpirationDate() != null
                && existing.getExpirationDate().isBefore(LocalDateTime.now());
        existing.setExpired(isExpired);

        UserPackage updated = userPackageRepository.save(existing);
        return mapper.toDTO(updated);
    }

    @Override
    public void delete(UUID id) {
        UserPackage entity = userPackageRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("UserPackage not found or already deleted"));
        entity.setDeleted(Boolean.TRUE);
        userPackageRepository.save(entity);
    }

    /**
     * Simulate purchasing a package for a user:
     * - Validate user and package exist
     * - Simulate payment success (mock)
     * - Create a new UserPackage with package credits and expiration date from Package entity
     * - Save and return DTO
     */
    @Override
    public UserPackageDTO purchasePackage(UUID userId, UUID packageId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Package pack = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        // Country code must match
        if (!user.getCountryCode().equalsIgnoreCase(pack.getCountryCode())) {
            throw new IllegalArgumentException("Package is not available for user's country");
        }

        // Mock payment process: always success
        boolean paymentSuccess = true;

        if (!paymentSuccess) {
            throw new RuntimeException("Payment failed");
        }

        UserPackage userPackage = buildUserPackage(user, pack, null, null);

        if (pack.getCountryCode() == null) {
            throw new RuntimeException("Package country code is null");
        }

        userPackage.setCountryCode(pack.getCountryCode());

        UserPackage saved = userPackageRepository.save(userPackage);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public boolean deductCredits(UUID userId, UUID packageId, int creditsToDeduct) {
        if (creditsToDeduct <= 0) {
            throw new IllegalArgumentException("Credits to deduct must be positive");
        }
        UserPackage userPackage = userPackageRepository.findByUserIdAndPackId(userId, packageId)
                .orElseThrow(() -> new IllegalArgumentException("UserPackage not found"));

        if (userPackage.getExpired() || userPackage.getRemainingCredits() < creditsToDeduct) {
            return false;
        }

        userPackage.setRemainingCredits(userPackage.getRemainingCredits() - creditsToDeduct);
        userPackageRepository.save(userPackage);
        return true;
    }

    @Override
    @Transactional
    public void refundCredits(UUID userId, UUID packageId, int creditsToRefund) {
        if (creditsToRefund <= 0) {
            throw new IllegalArgumentException("Credits to refund must be positive");
        }
        UserPackage userPackage = userPackageRepository.findByUserIdAndPackId(userId, packageId)
                .orElseThrow(() -> new IllegalArgumentException("UserPackage not found"));

        userPackage.setRemainingCredits(userPackage.getRemainingCredits() + creditsToRefund);
        userPackageRepository.save(userPackage);
    }

    private UserPackage buildUserPackage(User user, Package pack, Integer remainingCredits, LocalDateTime expirationDate) {
        UserPackage up = new UserPackage();
        up.setUser(user);
        up.setPack(pack);
        up.setRemainingCredits(remainingCredits != null ? remainingCredits : pack.getCredits());
        up.setExpirationDate(expirationDate != null ? expirationDate : pack.getExpirationDate());

        Boolean isExpired = up.getExpirationDate() != null && up.getExpirationDate().isBefore(LocalDateTime.now());
        up.setExpired(isExpired);
        return up;
    }
}
