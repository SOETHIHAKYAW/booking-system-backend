package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.demo.bookingsystem.dto.BookingDTO;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.BookingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/bookings")
@Tag(name = "Booking Management", description = "Operations related to booking management")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Operation(summary = "Create or update a booking", description = "Save or update a booking in the database.")
    public ResponseEntity<ApiResponse<BookingDTO>> createBooking(@RequestBody BookingDTO bookingDTO) {
        BookingDTO savedBooking = bookingService.createBooking(bookingDTO);
        ApiResponse<BookingDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Booking created successfully", savedBooking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get a booking by ID", description = "Fetch a booking from the database using its ID.")
    public ResponseEntity<ApiResponse<BookingDTO>> getBookingById(@PathVariable UUID id) {
        Optional<BookingDTO> bookingDTO = bookingService.getBookingById(id);
        if (bookingDTO.isPresent()) {
            ApiResponse<BookingDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Booking found", bookingDTO.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<BookingDTO> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Booking not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all bookings with pagination", description = "Fetch all bookings from the database with pagination support.")
    public ResponseEntity<ApiResponse<Page<BookingDTO>>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookingDTO> bookingsPage = bookingService.getAllBookings(pageable);

//        if (bookingsPage.hasContent()) {
            ApiResponse<Page<BookingDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Bookings found", bookingsPage);
            return ResponseEntity.ok(response);
//        } else {
//            ApiResponse<Page<BookingDTO>> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No bookings found", null);
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
//        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a booking by ID", description = "Update an existing booking in the database using its ID.")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(@PathVariable UUID id, @RequestBody BookingDTO bookingDTO) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
        ApiResponse<BookingDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Booking updated successfully", updatedBooking);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a booking by ID", description = "Delete a booking from the database using its ID.")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable UUID id) {
        bookingService.deleteBooking(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Booking deleted successfully", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PostMapping("/book")
    @Operation(summary = "Book a class", description = "User books a class with a valid schedule and package.")
    public ResponseEntity<ApiResponse<BookingDTO>> bookClass(
            @RequestParam UUID userId,
            @RequestParam UUID scheduleId) {

        BookingDTO booking = bookingService.bookClass(userId, scheduleId);
        ApiResponse<BookingDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Class booked successfully", booking);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "Cancel a booking", description = "User cancels their booking.")
    public ResponseEntity<ApiResponse<Void>> cancelBooking(
            @RequestParam UUID userId,
            @RequestParam UUID bookingId) {

        bookingService.cancelBooking(userId, bookingId);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK.value(), "Booking canceled successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-in")
    @Operation(summary = "Check in to a class", description = "User checks in for a scheduled class.")
    public ResponseEntity<ApiResponse<Void>> checkIn(
            @RequestParam UUID userId,
            @RequestParam UUID bookingId) {

        bookingService.checkIn(userId, bookingId);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK.value(), "Check-in successful", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end-class")
    @Operation(summary = "Handle end of class", description = "Performs post-class logic like processing waitlists.")
    public ResponseEntity<ApiResponse<Void>> handleClassEnd(@RequestParam UUID scheduleId) {
        bookingService.handleClassEnd(scheduleId);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK.value(), "Class end handled successfully", null);
        return ResponseEntity.ok(response);
    }

}