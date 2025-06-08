package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.demo.bookingsystem.dto.PackageDTO;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.PackageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * PackageController handles CRUD operations for packages and supports paginated retrieval.
 */
@RestController
@RequestMapping("api/v1/packages")
@Tag(name = "Package Management", description = "Operations related to package management")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    @Operation(summary = "Create a new package", description = "Create and store a new package in the system.")
    public ResponseEntity<ApiResponse<PackageDTO>> createPackage(@Valid @RequestBody PackageDTO packageDTO) {
        PackageDTO created = packageService.createPackage(packageDTO);
        ApiResponse<PackageDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Package created successfully", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a package", description = "Update an existing package using its ID.")
    public ResponseEntity<ApiResponse<PackageDTO>> updatePackage(@PathVariable UUID id, @Valid @RequestBody PackageDTO packageDTO) {
        PackageDTO updated = packageService.updatePackage(id, packageDTO);
        ApiResponse<PackageDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Package updated successfully", updated);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/id/{id}")
    @Operation(summary = "Get package by ID", description = "Retrieve a package using its unique ID.")
    public ResponseEntity<ApiResponse<PackageDTO>> getPackageById(@PathVariable UUID id) {
        PackageDTO found = packageService.getPackageById(id);
        ApiResponse<PackageDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Package found", found);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all packages (paginated)", description = "Fetch all packages with pagination.")
    public ResponseEntity<ApiResponse<Page<PackageDTO>>> getAllPackages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PackageDTO> packages = packageService.getAllPackages(PageRequest.of(page, size));
        if (packages.isEmpty()) {
            ApiResponse<Page<PackageDTO>> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No packages found", packages);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        ApiResponse<Page<PackageDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Packages retrieved successfully", packages);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a package", description = "Delete a package from the system using its ID.")
    public ResponseEntity<ApiResponse<Void>> deletePackage(@PathVariable UUID id) {
        packageService.deletePackage(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Package deleted successfully", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/available")
    @Operation(summary = "Get available packages by country", description = "Retrieve a list of available packages filtered by country code.")
    public ResponseEntity<ApiResponse<List<PackageDTO>>> getAvailablePackagesByCountry(
            @Valid @RequestParam String countryCode) {

        List<PackageDTO> packages = packageService.getAvailablePackagesByCountry(countryCode);

        if (packages.isEmpty()) {
            ApiResponse<List<PackageDTO>> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "No available packages found for country: " + countryCode,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<PackageDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Available packages retrieved successfully",
                packages
        );
        return ResponseEntity.ok(response);
    }
}