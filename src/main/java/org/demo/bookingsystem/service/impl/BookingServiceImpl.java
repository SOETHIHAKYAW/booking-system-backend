package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.BookingDTO;
import org.demo.bookingsystem.entity.*;
import org.demo.bookingsystem.enums.BookingStatus;
import org.demo.bookingsystem.exception.NotFoundException;
import org.demo.bookingsystem.mapper.BookingMapper;
import org.demo.bookingsystem.repository.*;
import org.demo.bookingsystem.service.BookingService;
import org.demo.bookingsystem.service.RedisLockService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final UserPackageRepository userPackageRepository;
    private final RedisLockService redisLockService;
    private final WaitlistRepository waitlistRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            ClassScheduleRepository classScheduleRepository,
            UserPackageRepository userPackageRepository, RedisLockService redisLockService,
            WaitlistRepository waitlistRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.classScheduleRepository = classScheduleRepository;
        this.userPackageRepository = userPackageRepository;
        this.redisLockService = redisLockService;
        this.waitlistRepository = waitlistRepository;
    }

    @Override
    public BookingDTO createBooking(BookingDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ClassSchedule classSchedule = classScheduleRepository.findById(dto.getClassScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Class schedule not found"));

        Booking booking = BookingMapper.toEntity(dto, user, classSchedule);
        Booking saved = bookingRepository.save(booking);
        return BookingMapper.toDto(saved);
    }

    @Override
    public Optional<BookingDTO> getBookingById(UUID id) {
        return bookingRepository.findById(id).map(BookingMapper::toDto);
    }

    @Override
    public Page<BookingDTO> getAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(BookingMapper::toDto);
    }

    @Override
    public BookingDTO updateBooking(UUID id, BookingDTO dto) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        ClassSchedule classSchedule = classScheduleRepository.findById(dto.getClassScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("Class schedule not found"));

        booking.setUser(user);
        booking.setClassSchedule(classSchedule);
        booking.setBookedAt(dto.getBookedAt());
        booking.setCanceled(dto.isCanceled());
        booking.setCheckedIn(dto.isCheckedIn());
        booking.setRefundedCredit(dto.isRefundedCredit());

        Booking updated = bookingRepository.save(booking);
        return BookingMapper.toDto(updated);
    }

    @Override
    public void deleteBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id " + id));

        booking.setCanceled(true);
        booking.setStatus(BookingStatus.DELETED);
        bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public BookingDTO bookClass(UUID userId, UUID scheduleId) {
        ClassSchedule classSchedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Class schedule not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 1. Validate package
        List<UserPackage> activePackages = userPackageRepository.findActivePackages(userId, classSchedule.getCountryCode(), LocalDateTime.now());

        UserPackage userPackage = activePackages.stream()
                .max(Comparator.comparing(UserPackage::getRemainingCredits))
                .orElseThrow(() -> new IllegalStateException("No valid package for this country"));

        if (userPackage.getRemainingCredits() < classSchedule.getRequiredCredits()) {
            throw new IllegalStateException("Not enough credits");
        }

        // 2. Prevent overlapping booking
        boolean hasOverlap = bookingRepository.existsOverlappingBooking(userId, classSchedule.getStartTime(), classSchedule.getEndTime());
        if (hasOverlap) {
            throw new IllegalStateException("You already booked a class at this time");
        }

        // 3. Acquire distributed lock (Redis)
        String lockKey = "lock:class:" + scheduleId;
        boolean locked = redisLockService.acquireLock(lockKey, 5); // lock for 5 seconds

        if (!locked) {
            throw new IllegalStateException("System busy, try again");
        }

        try {
            long currentBookings = bookingRepository.countByClassScheduleIdAndCanceledFalse(scheduleId);

            if (currentBookings < classSchedule.getMaxSlots()) {
                // Book directly
                Booking booking = new Booking();
                booking.setUser(user);
                booking.setClassSchedule(classSchedule);
                booking.setBookedAt(LocalDateTime.now());
                booking.setCanceled(false);
                booking.setCheckedIn(false);
                booking.setRefundedCredit(false);
                booking.setStatus(BookingStatus.BOOKED);

                bookingRepository.save(booking);

                // Deduct credit
                userPackage.setRemainingCredits(userPackage.getRemainingCredits() - classSchedule.getRequiredCredits());
                userPackageRepository.save(userPackage);

                return BookingMapper.toDto(booking);

            } else {
                // Add to waitlist
                Waitlist waitlist = new Waitlist();
                waitlist.setUser(user);
                waitlist.setClassSchedule(classSchedule);
                waitlist.setAddedAt(LocalDateTime.now());

                waitlistRepository.save(waitlist);
                throw new IllegalStateException("Class full. You've been added to the waitlist.");
            }

        } finally {
            redisLockService.releaseLock(lockKey);
        }
    }

    @Transactional
    @Override
    public void cancelBooking(UUID userId, UUID bookingId) {
        // 1. Get booking
        Booking booking = bookingRepository.findByIdAndUserIdAndCanceledFalse(bookingId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found or already canceled"));

        ClassSchedule schedule = booking.getClassSchedule();

        // 2. Check if user has an active package for that country
        List<UserPackage> activePackages = userPackageRepository.findActivePackages(userId, schedule.getCountryCode(), LocalDateTime.now());

        UserPackage userPackage = activePackages.stream()
                .max(Comparator.comparing(UserPackage::getRemainingCredits))
                .orElseThrow(() -> new IllegalStateException("No active package found for this booking"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime classStart = schedule.getStartTime();

        boolean refund = now.isBefore(classStart.minusHours(4)); // eligible for refund only if canceled 4h before

        // 3. Cancel booking
        booking.setCanceled(true);
        bookingRepository.save(booking);

        // 4. Refund credits if eligible
        if (refund) {
            userPackage.setRemainingCredits(userPackage.getRemainingCredits() + schedule.getRequiredCredits());
            userPackageRepository.save(userPackage);
            booking.setRefundedCredit(true);
            bookingRepository.save(booking); // save refund state
        }

        // 5. Move waitlist user (FIFO) to booked if available
        long bookedCount = bookingRepository.countByClassScheduleIdAndCanceledFalse(schedule.getId());
        if (bookedCount < schedule.getMaxSlots()) {
            Optional<Waitlist> waitlistOptional = waitlistRepository.findFirstByClassScheduleIdOrderByAddedAtAsc(schedule.getId());
            if (waitlistOptional.isPresent()) {
                Waitlist waitlist = waitlistOptional.get();

                Booking newBooking = new Booking();
                newBooking.setUser(waitlist.getUser());
                newBooking.setClassSchedule(schedule);
                newBooking.setBookedAt(LocalDateTime.now());
                newBooking.setCanceled(false);
                newBooking.setCheckedIn(false);
                newBooking.setRefundedCredit(false);

                bookingRepository.save(newBooking);

                // Deduct credits from new user
                UserPackage waitlistUserPackage = activePackages.stream()
                        .max(Comparator.comparing(UserPackage::getRemainingCredits))
                        .orElseThrow(() -> new IllegalStateException("No valid package for waitlist user"));

                if (waitlistUserPackage.getRemainingCredits() < schedule.getRequiredCredits()) {
                    throw new IllegalStateException("Waitlist user does not have enough credits");
                }

                waitlistUserPackage.setRemainingCredits(waitlistUserPackage.getRemainingCredits() - schedule.getRequiredCredits());
                userPackageRepository.save(waitlistUserPackage);

                // Remove from waitlist
                waitlistRepository.delete(waitlist);
            }
        }
    }

    @Override
    public void checkIn(UUID userId, UUID bookingId) {
        Booking booking = bookingRepository.findByIdAndUserIdAndCanceledFalse(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        ClassSchedule schedule = booking.getClassSchedule();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Now: " + now);
        System.out.println("Class Start: " + schedule.getStartTime());
        System.out.println("Class End: " + schedule.getEndTime());

        if (now.isBefore(schedule.getStartTime()) || now.isAfter(schedule.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in is only allowed during the class time");
        }

        if (booking.getCheckedIn()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already checked in");
        }

        booking.setCheckedIn(true);
        booking.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void handleClassEnd(UUID scheduleId) {
        ClassSchedule schedule = classScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Schedule not found"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(schedule.getEndTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot end class before class end time");
        }

        // 1. Refund all waitlisted users
        List<Waitlist> waitlists = waitlistRepository.findAllByClassScheduleId(scheduleId);

        for (Waitlist waitlist : waitlists) {
            if (!waitlist.getRefunded()) {
                UserPackage userPackage = waitlist.getUserPackage();
                userPackage.setRemainingCredits(userPackage.getRemainingCredits() + schedule.getRequiredCredits());
                userPackageRepository.save(userPackage);

                waitlist.setRefunded(true);
                waitlistRepository.save(waitlist);
            }
        }

        // 2. Update booked users to CHECKED_IN or mark as No-Show
        List<Booking> bookings = bookingRepository.findAllByClassScheduleId(scheduleId);

        for (Booking booking : bookings) {
            if (!booking.getCanceled() && booking.getStatus() == BookingStatus.BOOKED) {
                if (!booking.getCheckedIn()) {
                    booking.setStatus(BookingStatus.NO_SHOW); // or keep as BOOKED
                }
                bookingRepository.save(booking);
            }
        }

        // 3. Mark schedule as completed
        schedule.setCompleted(true);
        classScheduleRepository.save(schedule);
    }
}