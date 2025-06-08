package org.demo.bookingsystem.dto;

import lombok.Data;

@Data
public class BookingResponseDTO {
    private boolean isWaitlist;
    private BookingDTO booking;
    private WaitlistDTO waitlist;
}