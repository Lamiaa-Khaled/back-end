package com.ems.ems_app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ems.ems_app.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.ems_app.dto.requestDTO.GroupRequestDTO;
import com.ems.ems_app.dto.responseDTO.GroupResponseDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.repos.GroupRepository;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public GroupResponseDTO getGroupById(UUID id) {
        Optional<Group> groupOptional = groupRepository.findById(id);
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            GroupResponseDTO groupResponseDTO = new GroupResponseDTO();
            groupResponseDTO.setId(group.getId());
            groupResponseDTO.setName(group.getName());
            groupResponseDTO.setDescription(group.getDescription());
            return groupResponseDTO;
        } else {
            throw new ResourceNotFoundException("Group not found with id: " + id);
        }
    }


    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public void createGroup(GroupRequestDTO groupRequestDTO) {
        Group group = new Group();
        group.setId(UUID.randomUUID()); // Generate UUID here
        group.setName(groupRequestDTO.getName());
        group.setDescription(groupRequestDTO.getDescription());
        groupRepository.save(group);
    }

    public void updateGroup(UUID id, GroupRequestDTO groupRequestDTO) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));

        group.setName(groupRequestDTO.getName());
        group.setDescription(groupRequestDTO.getDescription());
        groupRepository.update(group);
    }

    public void deleteGroup(UUID id) {
        groupRepository.deleteById(id);
    }
}