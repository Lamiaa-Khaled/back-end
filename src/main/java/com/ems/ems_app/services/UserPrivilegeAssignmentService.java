package com.ems.ems_app.services;

import com.ems.ems_app.dto.UserPrivilegeAssignmentDto;
import com.ems.ems_app.entities.UserPrivilegeAssignment;
import com.ems.ems_app.exception.ResourceNotFoundException;
import com.ems.ems_app.repos.UserPrivilegeAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserPrivilegeAssignmentService {

    private final UserPrivilegeAssignmentRepository userPrivilegeAssignmentRepository;

    @Autowired
    public UserPrivilegeAssignmentService(UserPrivilegeAssignmentRepository userPrivilegeAssignmentRepository) {
        this.userPrivilegeAssignmentRepository = userPrivilegeAssignmentRepository;
    }

    public UserPrivilegeAssignmentDto getUserPrivilegeAssignmentById(UUID id) {
        UserPrivilegeAssignment userPrivilegeAssignment = userPrivilegeAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserPrivilegeAssignment not found with id: " + id));
        return convertToDto(userPrivilegeAssignment);
    }

    public List<UserPrivilegeAssignmentDto> getAllUserPrivilegeAssignments() {
        List<UserPrivilegeAssignment> userPrivilegeAssignments = userPrivilegeAssignmentRepository.findAll();
        return userPrivilegeAssignments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserPrivilegeAssignmentDto createUserPrivilegeAssignment(UserPrivilegeAssignmentDto userPrivilegeAssignmentDto) {
        UserPrivilegeAssignment userPrivilegeAssignment = convertToEntity(userPrivilegeAssignmentDto);
        UserPrivilegeAssignment savedUserPrivilegeAssignment = userPrivilegeAssignmentRepository.save(userPrivilegeAssignment);
        return convertToDto(savedUserPrivilegeAssignment);
    }

    public UserPrivilegeAssignmentDto updateUserPrivilegeAssignment(UUID id, UserPrivilegeAssignmentDto userPrivilegeAssignmentDto) {
        UserPrivilegeAssignment existingUserPrivilegeAssignment = userPrivilegeAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserPrivilegeAssignment not found with id: " + id));

        existingUserPrivilegeAssignment.setUserId(userPrivilegeAssignmentDto.getUserId());
        existingUserPrivilegeAssignment.setPrivilegeId(userPrivilegeAssignmentDto.getPrivilegeId());

        UserPrivilegeAssignment updatedUserPrivilegeAssignment = userPrivilegeAssignmentRepository.save(existingUserPrivilegeAssignment);
        return convertToDto(updatedUserPrivilegeAssignment);
    }

    public void deleteUserPrivilegeAssignment(UUID id) {
        userPrivilegeAssignmentRepository.deleteById(id);
    }

    private UserPrivilegeAssignmentDto convertToDto(UserPrivilegeAssignment userPrivilegeAssignment) {
        UserPrivilegeAssignmentDto userPrivilegeAssignmentDto = new UserPrivilegeAssignmentDto();
        userPrivilegeAssignmentDto.setId(userPrivilegeAssignment.getId());
        userPrivilegeAssignmentDto.setUserId(userPrivilegeAssignment.getUserId());
        userPrivilegeAssignmentDto.setPrivilegeId(userPrivilegeAssignment.getPrivilegeId());
        return userPrivilegeAssignmentDto;
    }

    private UserPrivilegeAssignment convertToEntity(UserPrivilegeAssignmentDto userPrivilegeAssignmentDto) {
        UserPrivilegeAssignment userPrivilegeAssignment = new UserPrivilegeAssignment();
        userPrivilegeAssignment.setUserId(userPrivilegeAssignmentDto.getUserId());
        userPrivilegeAssignment.setPrivilegeId(userPrivilegeAssignmentDto.getPrivilegeId());
        return userPrivilegeAssignment;
    }
}

