package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.GroupDTO;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class GroupController {
   /* @Autowired
    private GroupService groupService;

    @GetMapping("/findAll")
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("/find{id}")
    public GroupDTO getGroupById(@PathVariable UUID id) {
        Optional<GroupDTO> groupDTO = groupService.getGroupById(id);
        return groupDTO.orElse(null);
    }

    @PostMapping("/create" )
    @ResponseStatus(HttpStatus.CREATED)
    public Group createGroup(@RequestBody GroupDTO groupDTO) {
        return groupService.createGroup(groupDTO);
    }*/
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    // جلب جميع الجروبات
    @GetMapping("findAll")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        return ResponseEntity.ok(groupService.findAll());
    }

    // إضافة جروب جديد
    @PostMapping("create")
    public ResponseEntity<Void> createGroup(@RequestBody GroupDTO groupDTO) {
        groupService.save(groupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    // Endpoint to update a group
    @PutMapping("/update {id}")
    public ResponseEntity<Group> updateGroup(@PathVariable UUID id, @RequestBody Group updatedGroup) {
        Group group = groupService.updateGroup(id, updatedGroup);
        return ResponseEntity.ok(group);
    }

    // Endpoint to delete all groups
    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllGroups() {
        groupService.deleteAllGroups();
        return ResponseEntity.ok("All groups deleted successfully");
    }
    @DeleteMapping("/delete {id}")
    public ResponseEntity<String> deleteGroup(@PathVariable UUID id) {
        groupService.deleteById(id);
        return ResponseEntity.ok("Group deleted successfully");
    }
    @GetMapping("/find {id}")
    public ResponseEntity<Group> getGroupById(@PathVariable UUID id) {
        Optional<Group> group = groupService.findById(id);
        return group.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
