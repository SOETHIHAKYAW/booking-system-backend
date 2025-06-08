package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.Role;
import org.demo.bookingsystem.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * Repository interface for interacting with the Role entity.
 * Provides methods to query the Role table.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Retrieves a role by its name.
     *
     * @param name The name of the role to be retrieved.
     * @return An Optional containing the Role if found, or empty if not.
     */
    Optional<Role> findByName(RoleName name);
}
