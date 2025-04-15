package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.requestDTO.AcademicYearCourseRequestDTO;
import com.ems.ems_app.dto.requestDTO.AssignCoursesRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearCourseResponseDTO;
import com.ems.ems_app.dto.responseDTO.AssignCoursesResponseDTO;
import com.ems.ems_app.services.AcademicYearCourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/academic-year-courses")
@RequiredArgsConstructor
public class AcademicYearCourseController {

    private final AcademicYearCourseService academicYearCourseService;

    @PostMapping("/assign-bulk") // Use a distinct path like /assign-bulk or /batch
    public ResponseEntity<AssignCoursesResponseDTO> assignCoursesToAcademicYearTerm(
            @Valid @RequestBody AssignCoursesRequestDTO requestDTO) throws SQLException { // Use the new DTOs
        AssignCoursesResponseDTO response = academicYearCourseService.assignCoursesToYearAndTerm(requestDTO);
        // Use CREATED status as we are creating new associations
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<AcademicYearCourseResponseDTO> createAcademicYearCourse(@RequestBody AcademicYearCourseRequestDTO requestDTO) {
        AcademicYearCourseResponseDTO createdCourse = academicYearCourseService.createCourse(requestDTO);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearCourseResponseDTO> getAcademicYearCourseById(@PathVariable UUID id) {
        AcademicYearCourseResponseDTO course = academicYearCourseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearCourseResponseDTO>> getAllAcademicYearCourses() {
        // Add RequestParam filters if needed (e.g., by academicYearId, courseCode)
        List<AcademicYearCourseResponseDTO> courses = academicYearCourseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcademicYearCourseResponseDTO> updateAcademicYearCourse(
            @PathVariable UUID id,
            @RequestBody AcademicYearCourseRequestDTO requestDTO) {
        AcademicYearCourseResponseDTO updatedCourse = academicYearCourseService.updateCourse(id, requestDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicYearCourse(@PathVariable UUID id) {
        academicYearCourseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
