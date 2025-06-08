package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface BookingService {
    BookingDTO createBooking(BookingDTO dto);

    Optional<BookingDTO> getBookingById(UUID id);

    Page<BookingDTO> getAllBookings(Pageable pageable);

    BookingDTO updateBooking(UUID id, BookingDTO dto);

    void deleteBooking(UUID id);

    BookingDTO bookClass(UUID userId, UUID scheduleId);

    void cancelBooking(UUID userId, UUID bookingId);

    void checkIn(UUID userId, UUID bookingId);

    void handleClassEnd(UUID scheduleId);
}