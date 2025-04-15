package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.requestDTO.AcademicYearCourseAdminRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearCourseAdminResponseDTO;
import com.ems.ems_app.services.AcademicYearCourseAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academic-year-course-admins")
@RequiredArgsConstructor
public class AcademicYearCourseAdminController {

    private final AcademicYearCourseAdminService academicYearCourseAdminService;

    @PostMapping
    public ResponseEntity<AcademicYearCourseAdminResponseDTO> createAdminAssignment(@RequestBody AcademicYearCourseAdminRequestDTO requestDTO) {
        AcademicYearCourseAdminResponseDTO createdAssignment = academicYearCourseAdminService.createAdminAssignment(requestDTO);
        return new ResponseEntity<>(createdAssignment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearCourseAdminResponseDTO> getAdminAssignmentById(@PathVariable UUID id) {
        AcademicYearCourseAdminResponseDTO assignment = academicYearCourseAdminService.getAdminAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearCourseAdminResponseDTO>> getAllAdminAssignments() {
        // Add RequestParam filters if needed (e.g., by adminId, courseCode)
        List<AcademicYearCourseAdminResponseDTO> assignments = academicYearCourseAdminService.getAllAdminAssignments();
        return ResponseEntity.ok(assignments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicYearCourseAdminResponseDTO> updateAdminAssignment(
            @PathVariable UUID id,
            @RequestBody AcademicYearCourseAdminRequestDTO requestDTO) {
        AcademicYearCourseAdminResponseDTO updatedAssignment = academicYearCourseAdminService.updateAdminAssignment(id, requestDTO);
        return ResponseEntity.ok(updatedAssignment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminAssignment(@PathVariable UUID id) {
        academicYearCourseAdminService.deleteAdminAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
