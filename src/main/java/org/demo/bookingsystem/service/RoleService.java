package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.RoleDTO;

import java.util.List;
import java.util.Optional;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * Service interface for managing roles in the system.
 * <p>
 * This interface defines methods for creating, retrieving, updating, and deleting roles.
 * The service handles the mapping of role data between the data transfer objects (DTOs)
 * and the underlying data entities. It provides a way to interact with role-related operations
 * such as saving roles, fetching roles by ID or retrieving all roles, and deleting roles by ID.
 * </p>
 * <p>
 * Methods in this interface are expected to be implemented by a service class to perform
 * the corresponding operations on the underlying database or data repository.
 * </p>
 */
public interface RoleService {

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Saves or updates a role in the database.
     *
     * @param roleDTO the role DTO to save or update
     * @return the saved or updated role DTO
     */
    RoleDTO saveRole(RoleDTO roleDTO);

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Fetches a role by its ID.
     *
     * @param id the ID of the role to fetch
     * @return the role DTO if found, or an empty optional if not
     */
    Optional<RoleDTO> getRoleById(String id);

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Fetches all roles.
     *
     * @return a list of role DTOs
     */
    Optional<List<RoleDTO>> getAllRoles();

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Deletes a role by its ID.
     *
     * @param id the ID of the role to delete
     */
    void deleteRole(String id);
}
