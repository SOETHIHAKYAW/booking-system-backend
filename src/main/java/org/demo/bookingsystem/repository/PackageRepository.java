package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {

    Page<Package> findByActiveTrue(Pageable pageable);

    List<Package> findByCountryCodeAndActiveTrue(String countryCode);

    Optional<Package> findByIdAndActiveTrue(UUID id);
}
