package org.demo.bookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WaitlistDTO {

    private UUID id;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Class Schedule ID is required")
    private UUID classScheduleId;

    @NotNull(message = "Added time is required")
    private LocalDateTime addedAt;

    private Boolean refunded = false;
}