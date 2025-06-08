package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.demo.bookingsystem.dto.WaitlistDTO;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.WaitlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * WaitlistController handles waitlist-related operations including creating, updating,
 * retrieving, deleting waitlist entries, and listing waitlists with pagination.
 */
@RestController
@RequestMapping("api/v1/waitlists")
@Tag(name = "Waitlist Management", description = "Operations related to waitlist management")
public class WaitlistController {

    private final WaitlistService waitlistService;

    public WaitlistController(WaitlistService waitlistService) {
        this.waitlistService = waitlistService;
    }

    @PostMapping
    @Operation(summary = "Create a waitlist entry", description = "Create a new waitlist entry in the database.")
    public ResponseEntity<ApiResponse<WaitlistDTO>> createWaitlist(@RequestBody WaitlistDTO waitlistDTO) {
        WaitlistDTO savedDTO = waitlistService.createWaitlist(waitlistDTO);
        ApiResponse<WaitlistDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Waitlist entry created successfully", savedDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get a waitlist entry by ID", description = "Fetch a waitlist entry using its ID.")
    public ResponseEntity<ApiResponse<WaitlistDTO>> getWaitlistById(@PathVariable UUID id) {
        Optional<WaitlistDTO> waitlistDTO = waitlistService.getWaitlistById(id);
        if (waitlistDTO.isPresent()) {
            ApiResponse<WaitlistDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Waitlist entry found", waitlistDTO.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<WaitlistDTO> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Waitlist entry not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    @Operation(summary = "Get all waitlist entries with pagination", description = "Fetch all waitlist entries, paginated.")
    public ResponseEntity<ApiResponse<Page<WaitlistDTO>>> getAllWaitlists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<WaitlistDTO> waitlistPage = waitlistService.getAllWaitlists(pageable);

        if (waitlistPage.hasContent()) {
            ApiResponse<Page<WaitlistDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Waitlist entries found", waitlistPage);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Page<WaitlistDTO>> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No waitlist entries found", waitlistPage);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
    }

    @PutMapping("/id/{id}")
    @Operation(summary = "Update a waitlist entry", description = "Update an existing waitlist entry by ID.")
    public ResponseEntity<ApiResponse<WaitlistDTO>> updateWaitlist(@PathVariable UUID id, @RequestBody WaitlistDTO waitlistDTO) {
        WaitlistDTO updatedDTO = waitlistService.updateWaitlist(id, waitlistDTO);
        ApiResponse<WaitlistDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Waitlist entry updated successfully", updatedDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a waitlist entry by ID", description = "Delete a waitlist entry using its ID.")
    public ResponseEntity<ApiResponse<Void>> deleteWaitlistById(@PathVariable UUID id) {
        waitlistService.deleteWaitlist(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Waitlist entry deleted successfully", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}