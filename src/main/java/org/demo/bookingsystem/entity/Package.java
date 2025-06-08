package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "packages")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;  // e.g. "Basic Package SG"

    @Column(nullable = false)
    private int credits;

    @Column(nullable = false)
    private double price;

    @Column(length = 2, nullable = false)
    private String countryCode; // "SG", "MM", etc.

    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private Boolean active;

    // users purchased this package
    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL)
    private List<UserPackage> purchasedByUsers;
}