package org.demo.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PackageDTO {
    private UUID id;

    @NotBlank(message = "Package name is required")
    private String name;

    @Min(value = 0, message = "Credits must be zero or positive")
    private int credits;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private double price;

    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
    @Schema(example = "SG", description = "Country code in ISO 3166-1 alpha-2 format, e.g. 'SG' for Singapore")
    private String countryCode;

    // expirationDate can be null if package does not expire, but if present must be future or today
    @FutureOrPresent(message = "Expiration date must be today or in the future")
    private LocalDateTime expirationDate;

    @Schema(description = "Indicates if the package is currently active")
    private Boolean active = true; // Default to true, can be set to false for soft delete

}
