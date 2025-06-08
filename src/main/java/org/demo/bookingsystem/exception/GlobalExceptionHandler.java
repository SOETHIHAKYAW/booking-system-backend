package org.demo.bookingsystem.exception;

import org.demo.bookingsystem.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<Void> response = new ApiResponse<>(400, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle unexpected runtime errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntime(RuntimeException ex) {
        ApiResponse<Void> response = new ApiResponse<>(500, "Internal server error: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> error = Map.of("error", ex.getMessage());
        ApiResponse<Map<String, String>> response = new ApiResponse<>(404, "User not found", error);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("statusCode", ex.getStatusCode().value());
        errorBody.put("message", ex.getReason());
        errorBody.put("data", null);
        return new ResponseEntity<>(errorBody, ex.getStatusCode());
    }
}