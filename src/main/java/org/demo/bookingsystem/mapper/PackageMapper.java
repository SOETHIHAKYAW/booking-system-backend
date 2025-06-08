package org.demo.bookingsystem.mapper;

import org.demo.bookingsystem.dto.PackageDTO;
import org.demo.bookingsystem.entity.Package;

public class PackageMapper {

    public static PackageDTO toDTO(Package pack) {
        if (pack == null) return null;

        PackageDTO dto = new PackageDTO();
        dto.setId(pack.getId());
        dto.setName(pack.getName());
        dto.setCredits(pack.getCredits());
        dto.setPrice(pack.getPrice());
        dto.setCountryCode(pack.getCountryCode());
        dto.setExpirationDate(pack.getExpirationDate());
        dto.setActive(pack.getActive());

        return dto;
    }

    public static Package toEntity(PackageDTO dto) {
        if (dto == null) return null;

        Package pack = new Package();

        if (dto.getId() != null) {
            pack.setId(dto.getId());
        }
        pack.setName(dto.getName());
        pack.setCredits(dto.getCredits());
        pack.setPrice(dto.getPrice());
        pack.setCountryCode(dto.getCountryCode().toUpperCase());
        pack.setExpirationDate(dto.getExpirationDate());
        pack.setActive(dto.getActive());

        return pack;
    }
}