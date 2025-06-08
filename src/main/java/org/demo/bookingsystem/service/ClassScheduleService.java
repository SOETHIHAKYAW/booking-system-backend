package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.ClassScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassScheduleService {
    ClassScheduleDTO create(ClassScheduleDTO dto);

    Optional<ClassScheduleDTO> getById(UUID id);

    Page<ClassScheduleDTO> getAll(Pageable pageable);

    ClassScheduleDTO update(UUID id, ClassScheduleDTO dto);

    void delete(UUID id);

    List<ClassScheduleDTO> getClassesByCountry(String countryCode);
}