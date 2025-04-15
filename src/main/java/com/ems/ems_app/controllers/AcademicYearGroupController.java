package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.requestDTO.AcademicYearGroupRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearGroupResponseDTO;
import com.ems.ems_app.services.AcademicYearGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academic-year-groups")
@RequiredArgsConstructor
public class AcademicYearGroupController {

    private final AcademicYearGroupService academicYearGroupService;

    @PostMapping
    public ResponseEntity<AcademicYearGroupResponseDTO> createAcademicYearGroup(@RequestBody AcademicYearGroupRequestDTO requestDTO) {
        AcademicYearGroupResponseDTO createdGroup = academicYearGroupService.createGroup(requestDTO);
        return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearGroupResponseDTO> getAcademicYearGroupById(@PathVariable UUID id) {
        AcademicYearGroupResponseDTO group = academicYearGroupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearGroupResponseDTO>> getAllAcademicYearGroups(
            @RequestParam(required = false) UUID academicYearId) { // Optional filter by academic year
        List<AcademicYearGroupResponseDTO> groups;
        if (academicYearId != null) {
            groups = academicYearGroupService.getGroupsByAcademicYear(academicYearId);
        } else {
            groups = academicYearGroupService.getAllGroups();
        }
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicYearGroupResponseDTO> updateAcademicYearGroup(
            @PathVariable UUID id,
            @RequestBody AcademicYearGroupRequestDTO requestDTO) {
        AcademicYearGroupResponseDTO updatedGroup = academicYearGroupService.updateGroup(id, requestDTO);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicYearGroup(@PathVariable UUID id) {
        academicYearGroupService.deleteGroup(id);
        return ResponseEntity.noContent().build(); // Standard practice for DELETE success
    }
}