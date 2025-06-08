package org.demo.bookingsystem.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserIdResponseDTO {

    private UUID userId;

    public UserIdResponseDTO(UUID userId) {
        this.userId = userId;
    }
}
