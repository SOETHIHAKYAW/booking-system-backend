package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, UUID> {

    Optional<Waitlist> findFirstByClassScheduleIdOrderByAddedAtAsc(UUID classScheduleId);

    List<Waitlist> findAllByClassScheduleId(UUID scheduleId);

}