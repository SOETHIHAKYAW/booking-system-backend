package org.demo.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClassScheduleDTO {

    private UUID id;

    @NotBlank(message = "Class name is required")
    private String className;

    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters")
    @Schema(example = "SG", description = "Country code in ISO 3166-1 alpha-2 format, e.g. 'SG' for Singapore")
    private String countryCode;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @Min(value = 0, message = "Required credits must be zero or positive")
    private int requiredCredits;

    @Min(value = 1, message = "Max slots must be at least 1")
    private int maxSlots;
}
