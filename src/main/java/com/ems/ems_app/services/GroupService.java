package com.ems.ems_app.services;

<<<<<<< HEAD
=======
import com.ems.ems_app.dto.GroupDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.repos.GroupRepository;
import org.springframework.stereotype.Service;

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import java.util.List;
import java.util.Optional;
import java.util.UUID;

<<<<<<< HEAD
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
=======
@Service
public class GroupService {
    private final GroupRepository groupRepository;

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

<<<<<<< HEAD
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


=======
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

<<<<<<< HEAD
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
=======
    public Optional<Group> getGroupById(UUID id) {
        return groupRepository.findById(id);
    }

    public Group createOrUpdateGroup(Group group) {
        return groupRepository.save(group);
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    public void deleteGroup(UUID id) {
        groupRepository.deleteById(id);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
