package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.RoleDTO;
import org.demo.bookingsystem.entity.Role;

public class RoleMapper {

    public static RoleDTO toDTO(Role role) {
        if (role == null) return null;

        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName().toString());
        dto.setDescription(role.getDescription());

        return dto;
    }

    public static Role toEntity(RoleDTO dto) {
        if (dto == null) return null;

        Role role = new Role();
        role.setId(dto.getId());
        role.setName(Enum.valueOf(org.demo.bookingsystem.enums.RoleName.class, dto.getName()));
        role.setDescription(dto.getDescription());

        return role;
    }
}