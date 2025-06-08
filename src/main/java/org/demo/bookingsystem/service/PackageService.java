package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.PackageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PackageService {
    Page<PackageDTO> getAllPackages(Pageable pageable);

    PackageDTO getPackageById(UUID id);

    PackageDTO createPackage(PackageDTO packageDTO);

    PackageDTO updatePackage(UUID id, PackageDTO packageDTO);

    void deletePackage(UUID id);

    List<PackageDTO> getAvailablePackagesByCountry(String countryCode);
}