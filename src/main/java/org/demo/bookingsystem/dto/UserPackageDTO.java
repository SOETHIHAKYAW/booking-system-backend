package org.demo.bookingsystem.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserPackageDTO {

    private UUID id;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Package ID is required")
    private UUID packageId;

    @NotNull(message = "Country code is required")
    @Min(value = 2, message = "Country code must be exactly 2 characters")
    private String countryCode;

    @Min(value = 0, message = "Remaining credits cannot be negative")
    private int remainingCredits;

    @NotNull(message = "Expiration date is required")
    @FutureOrPresent(message = "Expiration date must be today or in the future")
    private LocalDateTime expirationDate;

    private Boolean expired;
}