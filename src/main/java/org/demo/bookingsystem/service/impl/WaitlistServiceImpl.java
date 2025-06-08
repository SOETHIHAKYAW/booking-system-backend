package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.WaitlistDTO;
import org.demo.bookingsystem.entity.ClassSchedule;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.entity.Waitlist;
import org.demo.bookingsystem.mapper.WaitlistMapper;
import org.demo.bookingsystem.repository.ClassScheduleRepository;
import org.demo.bookingsystem.repository.UserRepository;
import org.demo.bookingsystem.repository.WaitlistRepository;
import org.demo.bookingsystem.service.WaitlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final UserRepository userRepository;
    private final ClassScheduleRepository classScheduleRepository;

    @Autowired
    public WaitlistServiceImpl(
            WaitlistRepository waitlistRepository,
            UserRepository userRepository,
            ClassScheduleRepository classScheduleRepository) {
        this.waitlistRepository = waitlistRepository;
        this.userRepository = userRepository;
        this.classScheduleRepository = classScheduleRepository;
    }

    @Override
    public WaitlistDTO createWaitlist(WaitlistDTO waitlistDTO) {
        Waitlist waitlist = WaitlistMapper.toEntity(waitlistDTO);

        // Resolve associations
        Optional<User> userOpt = userRepository.findById(waitlistDTO.getUserId());
        Optional<ClassSchedule> classScheduleOpt = classScheduleRepository.findById(waitlistDTO.getClassScheduleId());

        userOpt.ifPresent(waitlist::setUser);
        classScheduleOpt.ifPresent(waitlist::setClassSchedule);

        Waitlist saved = waitlistRepository.save(waitlist);
        return WaitlistMapper.toDTO(saved);
    }

    @Override
    public Optional<WaitlistDTO> getWaitlistById(UUID id) {
        return waitlistRepository.findById(id)
                .map(WaitlistMapper::toDTO);
    }

    @Override
    public Page<WaitlistDTO> getAllWaitlists(Pageable pageable) {
        return waitlistRepository.findAll(pageable)
                .map(WaitlistMapper::toDTO);
    }

    @Override
    public WaitlistDTO updateWaitlist(UUID id, WaitlistDTO waitlistDTO) {
        Optional<Waitlist> existingOpt = waitlistRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Waitlist entry not found with id " + id);
        }
        Waitlist existing = existingOpt.get();

        // Update associations if provided
        if (waitlistDTO.getUserId() != null) {
            userRepository.findById(waitlistDTO.getUserId()).ifPresent(existing::setUser);
        }
        if (waitlistDTO.getClassScheduleId() != null) {
            classScheduleRepository.findById(waitlistDTO.getClassScheduleId()).ifPresent(existing::setClassSchedule);
        }

        if (waitlistDTO.getAddedAt() != null) {
            existing.setAddedAt(waitlistDTO.getAddedAt());
        }

        Waitlist saved = waitlistRepository.save(existing);
        return WaitlistMapper.toDTO(saved);
    }

    @Override
    public void deleteWaitlist(UUID id) {
        waitlistRepository.deleteById(id);
    }
}