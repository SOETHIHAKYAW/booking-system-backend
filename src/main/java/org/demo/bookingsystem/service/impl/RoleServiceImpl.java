package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.dto.RoleDTO;
import org.demo.bookingsystem.entity.Role;
import org.demo.bookingsystem.mapper.RoleMapper;
import org.demo.bookingsystem.repository.RoleRepository;
import org.demo.bookingsystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * Implementation of the RoleService interface.
 * <p>
 * This service class is responsible for managing roles within the system. It provides functionality for
 * creating, retrieving, updating, and deleting roles. It interacts with the RoleRepository to perform
 * CRUD operations and uses the UserMapper to map between the Role entity and RoleDTO.
 * </p>
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Saves a new role.
     * <p>
     * This method accepts a RoleDTO, maps it to a Role entity, saves the entity using the RoleRepository,
     * and then maps the saved Role entity back to a RoleDTO to return.
     * </p>
     *
     * @param roleDTO the RoleDTO containing the details of the role to save
     * @return the saved role as a RoleDTO
     */
    @Override
    public RoleDTO saveRole(RoleDTO roleDTO) {
        Role role = RoleMapper.toEntity(roleDTO);  // Using UserMapper to map RoleDTO to Role
        Role savedRole = roleRepository.save(role);
        return RoleMapper.toDTO(savedRole);  // Using UserMapper to map Role to RoleDTO
    }

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Retrieves a role by its ID.
     * <p>
     * This method retrieves a Role entity by its ID, maps it to a RoleDTO, and returns it wrapped in an Optional.
     * </p>
     *
     * @param id the UUID of the role
     * @return an Optional containing the RoleDTO if found, or an empty Optional if not found
     */
    @Override
    public Optional<RoleDTO> getRoleById(String id) {
        return roleRepository.findById(UUID.fromString(id))
                .map(RoleMapper::toDTO);
    }

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Retrieves all roles.
     * <p>
     * This method retrieves all Role entities, maps each to a RoleDTO, and returns the list wrapped in an Optional.
     * If no roles are found, it returns an empty Optional.
     * </p>
     *
     * @return an Optional containing the list of RoleDTOs if roles are found, or an empty Optional if none exist
     */
    @Override
    public Optional<List<RoleDTO>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            return Optional.empty();
        }
        List<RoleDTO> roleDTOs = roles.stream()
                .map(RoleMapper::toDTO)
                .collect(Collectors.toList());
        return Optional.of(roleDTOs);
    }

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * <p>
     * Deletes a role by its ID.
     * <p>
     * This method deletes a role from the database by its ID. If the role exists, it is deleted without returning any value.
     * </p>
     *
     * @param id the UUID of the role to delete
     */
    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(UUID.fromString(id));
    }
}