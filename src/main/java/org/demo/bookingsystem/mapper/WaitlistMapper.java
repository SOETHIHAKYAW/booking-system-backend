package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.WaitlistDTO;
import org.demo.bookingsystem.entity.ClassSchedule;
import org.demo.bookingsystem.entity.User;
import org.demo.bookingsystem.entity.Waitlist;

public class WaitlistMapper {

    public static WaitlistDTO toDTO(Waitlist waitlist) {
        if (waitlist == null) return null;

        WaitlistDTO dto = new WaitlistDTO();
        dto.setId(waitlist.getId());
        dto.setUserId(waitlist.getUser() != null ? waitlist.getUser().getId() : null);
        dto.setClassScheduleId(waitlist.getClassSchedule() != null ? waitlist.getClassSchedule().getId() : null);
        dto.setAddedAt(waitlist.getAddedAt());
        dto.setRefunded(waitlist.getRefunded());

        return dto;
    }

    public static Waitlist toEntity(WaitlistDTO dto) {
        if (dto == null) return null;

        Waitlist waitlist = new Waitlist();
        waitlist.setId(dto.getId());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            waitlist.setUser(user);
        } else {
            waitlist.setUser(null);
        }

        if (dto.getClassScheduleId() != null) {
            ClassSchedule classSchedule = new ClassSchedule();
            classSchedule.setId(dto.getClassScheduleId());
            waitlist.setClassSchedule(classSchedule);
        } else {
            waitlist.setClassSchedule(null);
        }

        waitlist.setAddedAt(dto.getAddedAt());
        waitlist.setRefunded(dto.getRefunded() != null ? dto.getRefunded() : false); // default false if null

        return waitlist;
    }
}