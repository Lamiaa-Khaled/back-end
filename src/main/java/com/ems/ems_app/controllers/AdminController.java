package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.requestDTO.AdminRequestDTO;
import com.ems.ems_app.dto.responseDTO.AdminResponseDTO;
import com.ems.ems_app.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminResponseDTO> createAdmin(@Valid @RequestBody AdminRequestDTO adminRequestDTO) {
        AdminResponseDTO createdAdmin = adminService.createAdmin(adminRequestDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable UUID adminId) {
        AdminResponseDTO admin = adminService.getAdminById(adminId);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
        List<AdminResponseDTO> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable UUID adminId, @Valid @RequestBody AdminRequestDTO adminRequestDTO) {
        AdminResponseDTO updatedAdmin = adminService.updateAdmin(adminId, adminRequestDTO);
        return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID adminId) {
        adminService.deleteAdmin(adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
