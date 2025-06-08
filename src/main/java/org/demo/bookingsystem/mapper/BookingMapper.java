package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.BookingDTO;
import org.demo.bookingsystem.entity.Booking;
import org.demo.bookingsystem.entity.ClassSchedule;
import org.demo.bookingsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public static BookingDTO toDto(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setClassScheduleId(booking.getClassSchedule().getId());
        dto.setBookedAt(booking.getBookedAt());
        dto.setCanceled(Boolean.TRUE.equals(booking.getCanceled()));
        dto.setCheckedIn(Boolean.TRUE.equals(booking.getCheckedIn()));
        dto.setRefundedCredit(Boolean.TRUE.equals(booking.getRefundedCredit()));
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public static Booking toEntity(BookingDTO dto, User user, ClassSchedule classSchedule) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setUser(user);
        booking.setClassSchedule(classSchedule);
        booking.setBookedAt(dto.getBookedAt());
        booking.setCanceled(dto.isCanceled());
        booking.setCheckedIn(dto.isCheckedIn());
        booking.setRefundedCredit(dto.isRefundedCredit());
        return booking;
    }
}