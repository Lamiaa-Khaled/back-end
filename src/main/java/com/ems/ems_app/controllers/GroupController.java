package com.ems.ems_app.controllers;

<<<<<<< HEAD
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.ems_app.dto.requestDTO.GroupRequestDTO;
import com.ems.ems_app.dto.responseDTO.GroupResponseDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.services.GroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
=======
import com.ems.ems_app.dto.GroupDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.services.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> getGroupById(@PathVariable UUID id) {
        GroupResponseDTO group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping
    public ResponseEntity<Void> createGroup(@Valid @RequestBody GroupRequestDTO groupRequestDTO) {
        groupService.createGroup(groupRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGroup(@PathVariable UUID id, @Valid @RequestBody GroupRequestDTO groupRequestDTO) {
        groupService.updateGroup(id, groupRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
=======
    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable UUID id) {
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Group createGroup(@RequestBody GroupDTO groupDTO) {
        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        return groupService.createOrUpdateGroup(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable UUID id, @RequestBody GroupDTO groupDTO) {
        return groupService.getGroupById(id)
                .map(existingGroup -> {
                    existingGroup.setName(groupDTO.getName());
                    existingGroup.setDescription(groupDTO.getDescription());
                    return ResponseEntity.ok(groupService.createOrUpdateGroup(existingGroup));
                })
                .orElse(ResponseEntity.notFound().build());
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID id) {
        groupService.deleteGroup(id);
<<<<<<< HEAD
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
=======
        return ResponseEntity.noContent().build();
    }
}

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
