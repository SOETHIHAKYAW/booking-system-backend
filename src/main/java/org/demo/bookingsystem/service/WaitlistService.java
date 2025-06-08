package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.WaitlistDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface WaitlistService {
    WaitlistDTO createWaitlist(WaitlistDTO waitlistDTO);

    Optional<WaitlistDTO> getWaitlistById(UUID id);

    Page<WaitlistDTO> getAllWaitlists(Pageable pageable);

    WaitlistDTO updateWaitlist(UUID id, WaitlistDTO waitlistDTO);

    void deleteWaitlist(UUID id);
}