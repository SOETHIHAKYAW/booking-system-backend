package org.demo.bookingsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.demo.bookingsystem.dto.RoleDTO;
import org.demo.bookingsystem.response.ApiResponse;
import org.demo.bookingsystem.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * RoleController handles role-related operations including creating, updating, retrieving, and deleting roles.
 * It interacts with the RoleService to perform these actions and provides responses to the client.
 */
@RestController
@RequestMapping("api/v1/roles")
@Tag(name = "Role Management", description = "Operations related to role management")
public class RoleController {

    private final RoleService roleService;

    // Constructor injection for RoleService dependency
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Create or update a role in the database.
     *
     * @param roleDTO The role data to be saved or updated.
     * @return ResponseEntity containing the saved or updated RoleDTO in an ApiResponse.
     */
    @PostMapping
    @Operation(summary = "Create or update a role", description = "Save or update a role in the database.")
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO savedRoleDTO = roleService.saveRole(roleDTO);
        ApiResponse<RoleDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Role created successfully", savedRoleDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Retrieve a role by its ID.
     *
     * @param id The ID of the role to be fetched.
     * @return ResponseEntity containing the RoleDTO in an ApiResponse if found, or HTTP 404 Not Found if not.
     */
    @GetMapping("/id/{id}")
    @Operation(summary = "Get a role by ID", description = "Fetch a role from the database using its ID.")
    public ResponseEntity<ApiResponse<RoleDTO>> getRoleById(@PathVariable String id) {
        Optional<RoleDTO> roleDTO = roleService.getRoleById(id);
        if (roleDTO.isPresent()) {
            ApiResponse<RoleDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Role found", roleDTO.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<RoleDTO> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Retrieve all roles from the database.
     *
     * @return ResponseEntity containing a list of all RoleDTOs in an ApiResponse, or HTTP 204 No Content if none found.
     */
    @GetMapping
    @Operation(summary = "Get all roles", description = "Fetch all roles from the database.")
    public ResponseEntity<ApiResponse<List<RoleDTO>>> getAllRoles() {
        Optional<List<RoleDTO>> roleDTOs = roleService.getAllRoles();
        if (roleDTOs.isPresent() && !roleDTOs.get().isEmpty()) {
            ApiResponse<List<RoleDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Roles found", roleDTOs.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<List<RoleDTO>> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No roles found", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Delete a role by its ID.
     *
     * @param id The ID of the role to be deleted.
     * @return ResponseEntity with HTTP 204 No Content on successful deletion in an ApiResponse.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role by ID", description = "Delete a role from the database using its ID.")
    public ResponseEntity<ApiResponse<Void>> deleteRoleById(@PathVariable String id) {
        roleService.deleteRole(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Role deleted successfully", null);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}