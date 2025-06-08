package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * Repository interface for interacting with the User entity.
 * Provides methods to query the User table.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Retrieves a user by their username.
     *
     * @param username The username of the user to be retrieved.
     * @return An Optional containing the User if found, or empty if not.
     */
    Optional<User> findByUsername(String username); // Custom query method for username

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Retrieves a user by their email.
     *
     * @param email The email of the user to be retrieved.
     * @return An Optional containing the User if found, or empty if not.
     */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByVerificationCode(String verificationCode);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.purchasedPackages WHERE u.id = :id")
    Optional<User> findByIdWithPackages(@Param("id") UUID id);
}