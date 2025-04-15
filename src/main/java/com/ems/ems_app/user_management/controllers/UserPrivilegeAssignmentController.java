package com.ems.ems_app.user_management.controllers;

import com.ems.ems_app.user_management.dto.UserPrivilegeAssignmentDto;
import com.ems.ems_app.user_management.services.UserPrivilegeAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/userPrivilegeAssignments")
public class UserPrivilegeAssignmentController {

    private final UserPrivilegeAssignmentService userPrivilegeAssignmentService;

    @Autowired
    public UserPrivilegeAssignmentController(UserPrivilegeAssignmentService userPrivilegeAssignmentService) {
        this.userPrivilegeAssignmentService = userPrivilegeAssignmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPrivilegeAssignmentDto> getUserPrivilegeAssignmentById(@PathVariable UUID id) {
        UserPrivilegeAssignmentDto userPrivilegeAssignmentDto = userPrivilegeAssignmentService.getUserPrivilegeAssignmentById(id);
        return ResponseEntity.ok(userPrivilegeAssignmentDto);
    }

    @GetMapping
    public ResponseEntity<List<UserPrivilegeAssignmentDto>> getAllUserPrivilegeAssignments() {
        List<UserPrivilegeAssignmentDto> userPrivilegeAssignments = userPrivilegeAssignmentService.getAllUserPrivilegeAssignments();
        return ResponseEntity.ok(userPrivilegeAssignments);
    }

    @PostMapping
    public ResponseEntity<UserPrivilegeAssignmentDto> createUserPrivilegeAssignment(@RequestBody UserPrivilegeAssignmentDto userPrivilegeAssignmentDto) {
        UserPrivilegeAssignmentDto createdUserPrivilegeAssignment = userPrivilegeAssignmentService.createUserPrivilegeAssignment(userPrivilegeAssignmentDto);
        return new ResponseEntity<>(createdUserPrivilegeAssignment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserPrivilegeAssignmentDto> updateUserPrivilegeAssignment(@PathVariable UUID id, @RequestBody UserPrivilegeAssignmentDto userPrivilegeAssignmentDto) {
        UserPrivilegeAssignmentDto updatedUserPrivilegeAssignment = userPrivilegeAssignmentService.updateUserPrivilegeAssignment(id, userPrivilegeAssignmentDto);
        return ResponseEntity.ok(updatedUserPrivilegeAssignment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserPrivilegeAssignment(@PathVariable UUID id) {
        userPrivilegeAssignmentService.deleteUserPrivilegeAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
