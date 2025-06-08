package org.demo.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.demo.bookingsystem.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "class_schedule_id")
    private ClassSchedule classSchedule;

    private LocalDateTime bookedAt;

    private Boolean canceled = false;

    private Boolean checkedIn = false;

    private Boolean refundedCredit = false;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}

