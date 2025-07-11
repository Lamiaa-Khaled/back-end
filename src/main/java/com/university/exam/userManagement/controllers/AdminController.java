package com.university.exam.userManagement.controllers;


import com.university.exam.userManagement.dtos.requestDTO.AdminRequestDTO;
import com.university.exam.userManagement.dtos.responseDTO.AdminResponseDTO;
import com.university.exam.userManagement.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'VIEWER')")
    @Operation(
            summary = "Get admin by user ID",
            description = "Retrieves an admin by the associated user ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admin retrieved successfully",
                            content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Admin not found")
            }
    )
    public ResponseEntity<AdminResponseDTO> getAdminByUserId(@PathVariable UUID userId) throws Exception {
        return ResponseEntity.ok(adminService.getAdminByUserId(userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'VIEWER')")
    @Operation(
            summary = "Get all admins",
            description = "Retrieves all admins.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admins retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AdminResponseDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Admins not found")
            }
    )
    public ResponseEntity<List<AdminResponseDTO>> getAdmins() throws Exception {
        return ResponseEntity.ok(adminService.getAdmins());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @Operation(
            summary = "Create a new admin",
            description = "Create a new admin with the provided details.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Admin created successfully",
                            content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload")
            }
    )
    public ResponseEntity<AdminResponseDTO> createAdmin(@Valid @RequestBody AdminRequestDTO adminRequestDTO) throws Exception {
        return ResponseEntity.ok(adminService.createAdmin(adminRequestDTO));
    }
}
