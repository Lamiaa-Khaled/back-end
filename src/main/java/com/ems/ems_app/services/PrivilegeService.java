package com.ems.ems_app.services;

import com.ems.ems_app.dto.PrivilegeDto;
import com.ems.ems_app.entities.Privilege;
import com.ems.ems_app.exception.ResourceNotFoundException;
import com.ems.ems_app.repos.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeService(PrivilegeRepository privilegeRepository) {
        this.privilegeRepository = privilegeRepository;
    }

    public PrivilegeDto getPrivilegeById(UUID id) {
        Privilege privilege = privilegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Privilege not found with id: " + id));
        return convertToDto(privilege);
    }

    public List<PrivilegeDto> getAllPrivileges() {
        List<Privilege> privileges = privilegeRepository.findAll();
        return privileges.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PrivilegeDto createPrivilege(PrivilegeDto privilegeDto) {
        Privilege privilege = convertToEntity(privilegeDto);
        Privilege savedPrivilege = privilegeRepository.save(privilege);
        return convertToDto(savedPrivilege);
    }

    public PrivilegeDto updatePrivilege(UUID id, PrivilegeDto privilegeDto) {
        Privilege existingPrivilege = privilegeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Privilege not found with id: " + id));

        existingPrivilege.setDescription(privilegeDto.getDescription());
        existingPrivilege.setUserId(privilegeDto.getUserId());
        existingPrivilege.setRoleId(privilegeDto.getRoleId());

        Privilege updatedPrivilege = privilegeRepository.save(existingPrivilege);
        return convertToDto(updatedPrivilege);
    }

    public void deletePrivilege(UUID id) {
        privilegeRepository.deleteById(id);
    }

    private PrivilegeDto convertToDto(Privilege privilege) {
        PrivilegeDto privilegeDto = new PrivilegeDto();
        privilegeDto.setId(privilege.getId());
        privilegeDto.setDescription(privilege.getDescription());
        privilegeDto.setUserId(privilege.getUserId());
        privilegeDto.setRoleId(privilege.getRoleId());
        return privilegeDto;
    }

    private Privilege convertToEntity(PrivilegeDto privilegeDto) {
        Privilege privilege = new Privilege();
        privilege.setDescription(privilegeDto.getDescription());
        privilege.setUserId(privilegeDto.getUserId());
        privilege.setRoleId(privilegeDto.getRoleId());
        return privilege;
    }
}
