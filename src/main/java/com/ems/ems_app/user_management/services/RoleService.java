package com.ems.ems_app.user_management.services;

import com.ems.ems_app.user_management.dto.RoleDto;
import com.ems.ems_app.user_management.entities.Role;
import com.ems.ems_app.user_management.exception.ResourceNotFoundException;
import com.ems.ems_app.user_management.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDto getRoleById(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return convertToDto(role);
    }

    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public RoleDto createRole(RoleDto roleDto) {
        Role role = convertToEntity(roleDto);
        Role savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }

    public RoleDto updateRole(UUID id, RoleDto roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        existingRole.setDescription(roleDto.getDescription());
        existingRole.setDetails(roleDto.getDetails());

        Role updatedRole = roleRepository.save(existingRole);
        return convertToDto(updatedRole);
    }

    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }

    private RoleDto convertToDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setRoleId(role.getRoleId());
        roleDto.setDescription(role.getDescription());
        roleDto.setDetails(role.getDetails());
        return roleDto;
    }

    private Role convertToEntity(RoleDto roleDto) {
        Role role = new Role();
        role.setDescription(roleDto.getDescription());
        role.setDetails(roleDto.getDetails());
        return role;
    }
}
