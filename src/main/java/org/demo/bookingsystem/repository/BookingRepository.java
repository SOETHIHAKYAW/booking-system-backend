package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Booking b " +
            "WHERE b.user.id = :userId AND b.canceled = false " +
            "AND b.classSchedule.startTime BETWEEN :start AND :end")
    boolean existsOverlappingBooking(@Param("userId") UUID userId,
                                     @Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end);

    long countByClassScheduleIdAndCanceledFalse(UUID classScheduleId);

    Optional<Booking> findByIdAndUserIdAndCanceledFalse(UUID bookingId, UUID userId);

    List<Booking> findAllByClassScheduleId(UUID scheduleId);

}