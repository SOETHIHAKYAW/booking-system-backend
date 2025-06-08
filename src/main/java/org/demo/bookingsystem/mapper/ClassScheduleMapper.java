package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.ClassScheduleDTO;
import org.demo.bookingsystem.entity.ClassSchedule;
import org.springframework.stereotype.Component;

@Component
public class ClassScheduleMapper {

    public ClassScheduleDTO toDTO(ClassSchedule entity) {
        if (entity == null) return null;

        ClassScheduleDTO dto = new ClassScheduleDTO();
        dto.setId(entity.getId());
        dto.setClassName(entity.getClassName());
        dto.setCountryCode(entity.getCountryCode());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setRequiredCredits(entity.getRequiredCredits());
        dto.setMaxSlots(entity.getMaxSlots());
        return dto;
    }

    public ClassSchedule toEntity(ClassScheduleDTO dto) {
        if (dto == null) return null;

        ClassSchedule entity = new ClassSchedule();
        entity.setId(dto.getId());
        entity.setClassName(dto.getClassName());
        entity.setCountryCode(dto.getCountryCode());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setRequiredCredits(dto.getRequiredCredits());
        entity.setMaxSlots(dto.getMaxSlots());
        return entity;
    }

    public void updateEntity(ClassSchedule entity, ClassScheduleDTO dto) {
        entity.setClassName(dto.getClassName());
        entity.setCountryCode(dto.getCountryCode());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setRequiredCredits(dto.getRequiredCredits());
        entity.setMaxSlots(dto.getMaxSlots());
    }

    public ClassScheduleDTO toDTOWithAvailability(ClassSchedule entity, int availableSlots) {
        if (entity == null) return null;

        ClassScheduleDTO dto = toDTO(entity);
        dto.setMaxSlots(availableSlots);
        return dto;
    }
}