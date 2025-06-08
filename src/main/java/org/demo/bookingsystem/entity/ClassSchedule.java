package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "class_schedules")
public class ClassSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private int requiredCredits;

    @Column(nullable = false)
    private int maxSlots;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean completed = false;

    @OneToMany(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "classSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Waitlist> waitlist;
}
