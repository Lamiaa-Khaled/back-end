package com.ems.ems_app.services;

import com.ems.ems_app.dto.GroupDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.repos.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(UUID id) {
        return groupRepository.findById(id);
    }

    public Group createOrUpdateGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(UUID id) {
        groupRepository.deleteById(id);
    }
}
