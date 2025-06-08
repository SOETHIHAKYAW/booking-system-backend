package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.ClassScheduleDTO;
import org.demo.bookingsystem.entity.ClassSchedule;
import org.demo.bookingsystem.mapper.ClassScheduleMapper;
import org.demo.bookingsystem.repository.BookingRepository;
import org.demo.bookingsystem.repository.ClassScheduleRepository;
import org.demo.bookingsystem.service.ClassScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {

    private final ClassScheduleRepository repository;
    private final BookingRepository bookingRepository;
    private final ClassScheduleMapper mapper;

    public ClassScheduleServiceImpl(ClassScheduleRepository repository, BookingRepository bookingRepository, ClassScheduleMapper mapper) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
    }

    @Override
    public ClassScheduleDTO create(ClassScheduleDTO dto) {
        ClassSchedule entity = mapper.toEntity(dto);
        entity.setActive(true); // Set active to true by default
        return mapper.toDTO(repository.save(entity));
    }

    @Override
    public Optional<ClassScheduleDTO> getById(UUID id) {
        return repository.findById(id).map(mapper::toDTO);
    }

    @Override
    public Page<ClassScheduleDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public ClassScheduleDTO update(UUID id, ClassScheduleDTO dto) {
        return repository.findById(id)
                .map(existing -> {
                    mapper.updateEntity(existing, dto);
                    return mapper.toDTO(repository.save(existing));
                })
                .orElseThrow(() -> new IllegalArgumentException("ClassSchedule not found with id " + id));
    }

    @Override
    public void delete(UUID id) {
        ClassSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ClassSchedule not found with id " + id));

        // Soft delete by setting active to false
        schedule.setActive(false);

        repository.save(schedule);
    }

    @Override
    public List<ClassScheduleDTO> getClassesByCountry(String countryCode) {
        List<ClassSchedule> schedules = repository.findByCountryCode(countryCode);

        return schedules.stream().map(schedule -> {
            long booked = bookingRepository.countByClassScheduleIdAndCanceledFalse(schedule.getId());
            int availableSlots = schedule.getMaxSlots() - (int) booked;

            return mapper.toDTOWithAvailability(schedule, availableSlots);
        }).toList();
    }
}