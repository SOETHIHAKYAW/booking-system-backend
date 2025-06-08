package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.PackageDTO;
import org.demo.bookingsystem.entity.Package;
import org.demo.bookingsystem.exception.ResourceNotFoundException;
import org.demo.bookingsystem.mapper.PackageMapper;
import org.demo.bookingsystem.repository.PackageRepository;
import org.demo.bookingsystem.service.PackageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public Page<PackageDTO> getAllPackages(Pageable pageable) {
        return packageRepository.findByActiveTrue(pageable)
                .map(PackageMapper::toDTO);
    }

    @Override
    public PackageDTO getPackageById(UUID id) {
        Package pack = packageRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        return PackageMapper.toDTO(pack);
    }

    @Override
    public PackageDTO createPackage(PackageDTO dto) {
        Package pack = PackageMapper.toEntity(dto);

        pack.setActive(true); // Set active to true by default
        return PackageMapper.toDTO(packageRepository.save(pack));
    }

    @Override
    public PackageDTO updatePackage(UUID id, PackageDTO dto) {
        Package existing = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

        existing.setName(dto.getName());
        existing.setCredits(dto.getCredits());
        existing.setPrice(dto.getPrice());
        existing.setCountryCode(dto.getCountryCode());
        existing.setExpirationDate(dto.getExpirationDate());

        return PackageMapper.toDTO(packageRepository.save(existing));
    }

    @Override
    public void deletePackage(UUID id) {
        Package existing = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));
        existing.setActive(false); // Soft delete
        packageRepository.save(existing);
    }

    @Override
    public List<PackageDTO> getAvailablePackagesByCountry(String countryCode) {

        if (countryCode == null || countryCode.isEmpty()) {
            throw new IllegalArgumentException("Country code must not be null or empty");
        }
        if (countryCode.length() != 2) {
            throw new IllegalArgumentException("Country code must be exactly 2 characters");
        }

        List<Package> packages = packageRepository.findByCountryCodeAndActiveTrue(countryCode.toUpperCase());
        return packages.stream()
                .map(PackageMapper::toDTO)
                .toList();
    }
}