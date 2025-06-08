package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.UserPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, UUID> {

    @Query("SELECT up FROM UserPackage up WHERE up.deleted = false OR up.deleted IS NULL")
    Page<UserPackage> findAllNotDeleted(Pageable pageable);

    @Query("SELECT up FROM UserPackage up WHERE up.id = :id AND (up.deleted = false OR up.deleted IS NULL)")
    Optional<UserPackage> findByIdAndNotDeleted(@Param("id") UUID id);

    Optional<UserPackage> findByUserIdAndPackId(UUID userId, UUID packageId);

    @Query("SELECT up FROM UserPackage up WHERE up.user.id = :userId " +
            "AND up.countryCode = :countryCode " +
            "AND up.expirationDate >= :now " +
            "AND (up.expired = false OR up.expired IS NULL) " +
            "AND (up.deleted = false OR up.deleted IS NULL)")
    List<UserPackage> findActivePackages(@Param("userId") UUID userId,
                                         @Param("countryCode") String countryCode,
                                         @Param("now") LocalDateTime now);

}