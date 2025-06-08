package org.demo.bookingsystem.repository;

import org.demo.bookingsystem.entity.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, UUID> {

    @Query("""
                SELECT c FROM ClassSchedule c
                WHERE c.countryCode = :countryCode AND c.active = true
                ORDER BY c.startTime ASC
            """)
    List<ClassSchedule> findByCountryCode(@Param("countryCode") String countryCode);

}