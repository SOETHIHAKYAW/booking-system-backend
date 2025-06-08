package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_packages")
public class UserPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package pack;

    @Column(nullable = false)
    private String countryCode;

    private int remainingCredits;

    private LocalDateTime expirationDate;

    private Boolean expired;

    private Boolean deleted = false;
}

