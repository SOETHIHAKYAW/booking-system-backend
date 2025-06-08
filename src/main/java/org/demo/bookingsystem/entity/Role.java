package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.demo.bookingsystem.enums.RoleName;

import java.util.UUID;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * The Role entity represents a user role in the system, such as "Admin" or "User".
 * It includes a unique name, a description, and other properties related to the role.
 */
@Entity
@Data
public class Role {

    /**
     * The unique identifier for the role
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The name of the role, such as Admin, User, etc.
     */
    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    /**
     * A description of the role, explaining its purpose (e.g., Admin with full access)
     */
    @Column(length = 512)
    private String description;
}
