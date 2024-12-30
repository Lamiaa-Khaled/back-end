package com.ems.ems_app.services;

import com.ems.ems_app.dto.GroupDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.repos.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {


    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    // جلب جميع الجروبات
    public List<GroupDTO> findAll() {
        return groupRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional  // Ensure the method is transactional

    // حفظ جروب جديد
    public void save(GroupDTO dto) {
        Group group = mapToEntity(dto);
        groupRepository.save(group);
    }

    // التحويل من Entity إلى DTO
    private GroupDTO mapToDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        return dto;
    }

    // التحويل من DTO إلى Entity
    private Group mapToEntity(GroupDTO dto) {
        Group group = new Group();
        group.setId(UUID.randomUUID()); // إنشاء UUID جديد للجروب
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());
        return group;
    }
    // Method to update a group
    public Group updateGroup(UUID id, Group updatedGroup) {
        Group group = groupRepository.updateGroup(id, updatedGroup);
        if (group != null) {
            return group;
        } else {
            throw new RuntimeException("Group not found with ID: " + id);
        }
    }

    // Method to delete all groups
    public void deleteAllGroups() {
        groupRepository.deleteAllGroups();
    }
    // Method to delete a group by ID
    public void deleteById(UUID id) {
        groupRepository.deleteById(id);
    }
    // Method to find a group by ID
    public Optional<Group> findById(UUID id) {
        return groupRepository.findById(id);
    }



}


