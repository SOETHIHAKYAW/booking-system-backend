package org.demo.bookingsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.demo.bookingsystem.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingDTO {
    private UUID id;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Class Schedule ID is required")
    private UUID classScheduleId;

    private LocalDateTime bookedAt;
    private boolean canceled;
    private boolean checkedIn;
    private boolean refundedCredit;
    private BookingStatus status;
}