package com.ems.ems_app.user_management.controllers;

import com.ems.ems_app.user_management.dto.PrivilegeDto;
import com.ems.ems_app.user_management.services.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/privileges")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @Autowired
    public PrivilegeController(PrivilegeService privilegeService) {
        this.privilegeService = privilegeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrivilegeDto> getPrivilegeById(@PathVariable UUID id) {
        PrivilegeDto privilegeDto = privilegeService.getPrivilegeById(id);
        return ResponseEntity.ok(privilegeDto);
    }

    @GetMapping
    public ResponseEntity<List<PrivilegeDto>> getAllPrivileges() {
        List<PrivilegeDto> privileges = privilegeService.getAllPrivileges();
        return ResponseEntity.ok(privileges);
    }

    @PostMapping
    public ResponseEntity<PrivilegeDto> createPrivilege(@RequestBody PrivilegeDto privilegeDto) {
        PrivilegeDto createdPrivilege = privilegeService.createPrivilege(privilegeDto);
        return new ResponseEntity<>(createdPrivilege, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrivilegeDto> updatePrivilege(@PathVariable UUID id, @RequestBody PrivilegeDto privilegeDto) {
        PrivilegeDto updatedPrivilege = privilegeService.updatePrivilege(id, privilegeDto);
        return ResponseEntity.ok(updatedPrivilege);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrivilege(@PathVariable UUID id) {
        privilegeService.deletePrivilege(id);
        return ResponseEntity.noContent().build();
    }
}