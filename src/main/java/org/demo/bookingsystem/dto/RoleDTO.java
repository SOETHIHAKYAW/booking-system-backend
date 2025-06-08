package org.demo.bookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * This class represents a data transfer object for Role.
 * It contains the details of a role including its ID, name, and description.
 */
@Data
public class RoleDTO {

    /**
     * The unique identifier for the role
     */
    private UUID id;

    /**
     * The name of the role
     */
    @Schema(example = "ROLE_USER", description = "The name of the role")
    @NotBlank(message = "Role name is required")
    private String name;

    /**
     * A description of the role
     */
    private String description;
}