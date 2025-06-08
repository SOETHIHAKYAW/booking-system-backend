package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.demo.bookingsystem.dto.ClassScheduleDTO;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.ClassScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * ClassScheduleController handles class scheduling operations such as creation, retrieval, update, and deletion.
 * It provides paginated listings and uses ApiResponse to wrap responses uniformly.
 */
@RestController
@RequestMapping("api/v1/class-schedules")
@Tag(name = "Class Schedule Management", description = "Operations related to managing class schedules")
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    public ClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    /**
     * Create a new class schedule.
     *
     * @param dto ClassScheduleDTO payload
     * @return Created ClassScheduleDTO wrapped in ApiResponse
     */
    @PostMapping
    @Operation(summary = "Create a class schedule", description = "Create a new class schedule in the system.")
    public ResponseEntity<ApiResponse<ClassScheduleDTO>> create(@RequestBody ClassScheduleDTO dto) {
        ClassScheduleDTO created = classScheduleService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Class schedule created successfully", created));
    }

    /**
     * Get class schedule by ID.
     *
     * @param id UUID of class schedule
     * @return ClassScheduleDTO wrapped in ApiResponse or 404 if not found
     */
    @GetMapping("/id/{id}")
    @Operation(summary = "Get class schedule by ID", description = "Retrieve a class schedule using its unique ID.")
    public ResponseEntity<ApiResponse<ClassScheduleDTO>> getById(@PathVariable UUID id) {
        Optional<ClassScheduleDTO> optional = classScheduleService.getById(id);
        return optional.map(dto ->
                ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Class schedule found", dto))
        ).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Class schedule not found", null))
        );
    }

    /**
     * Get paginated list of class schedules.
     *
     * @param page Page number (default 0)
     * @param size Page size (default 10)
     * @return Paginated class schedules wrapped in ApiResponse
     */
    @GetMapping
    @Operation(summary = "Get all class schedules (paginated)", description = "Fetch paginated class schedules from the database.")
    public ResponseEntity<ApiResponse<Page<ClassScheduleDTO>>> getAll(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClassScheduleDTO> result = classScheduleService.getAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Class schedules fetched", result));
    }

    /**
     * Update an existing class schedule by ID.
     *
     * @param id  UUID of class schedule
     * @param dto Updated values
     * @return Updated ClassScheduleDTO wrapped in ApiResponse
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a class schedule", description = "Update an existing class schedule by its ID.")
    public ResponseEntity<ApiResponse<ClassScheduleDTO>> update(@PathVariable UUID id, @RequestBody ClassScheduleDTO dto) {
        ClassScheduleDTO updated = classScheduleService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Class schedule updated successfully", updated));
    }

    /**
     * Delete class schedule by ID.
     *
     * @param id UUID of class schedule
     * @return 204 No Content in ApiResponse
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a class schedule", description = "Remove a class schedule from the system.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        classScheduleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Class schedule deleted", null));
    }

    /**
     * Get class schedules by country code.
     *
     * @param country Country code to filter schedules
     * @return List of ClassScheduleDTOs for the specified country
     */
    @GetMapping("/classes")
    public ResponseEntity<List<ClassScheduleDTO>> getClassesByCountry(
            @RequestParam String country) {
        return ResponseEntity.ok(classScheduleService.getClassesByCountry(country));
    }
}