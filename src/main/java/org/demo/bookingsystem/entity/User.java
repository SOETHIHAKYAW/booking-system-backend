package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * The User entity represents a user within the system. It includes basic information
 * such as username, email, password, and role, as well as optional fields for the user's
 * name, phone, wallet address, and deletion timestamp.
 */
@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private Boolean emailVerified;

    private String verificationCode;

    private Boolean active;

    @Column(length = 2)  // ISO country code, e.g. "SG", "MM"
    private String countryCode;

    /**
     * The role assigned to the user
     * FetchType.EAGER because roles are usually needed on login/authentication
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    // One user can have multiple purchased packages
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserPackage> purchasedPackages;

    // One user can have multiple bookings
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Waitlist> waitlists;

    /**
     * The date and time the user was created
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * The date and time the user information was last updated
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * The date and time the user was deleted, used for soft deletion
     */
    private LocalDateTime deletedAt;
}