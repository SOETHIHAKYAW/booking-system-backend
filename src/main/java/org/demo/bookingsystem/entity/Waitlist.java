package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "waitlists")
public class Waitlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_schedule_id")
    private ClassSchedule classSchedule;

    private LocalDateTime addedAt;

    @ManyToOne
    @JoinColumn(name = "user_package_id")
    private UserPackage userPackage;

    @Column(nullable = false)
    private Boolean refunded = false;
}


