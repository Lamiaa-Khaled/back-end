package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.responseDTO.ClassStudyResponseDTO;
import com.ems.ems_app.services.ClassStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/class-studies")
public class ClassStudyController {

    private final ClassStudyService classStudyService;

    @Autowired
    public ClassStudyController(ClassStudyService classStudyService) {
        this.classStudyService = classStudyService;
    }

    @PostMapping
    public ResponseEntity<ClassStudyResponseDTO> createClassStudy(
            @RequestParam int year,
            @RequestParam BigDecimal totalGrade) {
        ClassStudyResponseDTO createdClassStudy = classStudyService.createClassStudy(year, totalGrade);
        return new ResponseEntity<>(createdClassStudy, HttpStatus.CREATED);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassStudyResponseDTO> getClassStudyById(@PathVariable UUID classId) {
        ClassStudyResponseDTO classStudy = classStudyService.getClassStudyById(classId);
        return new ResponseEntity<>(classStudy, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ClassStudyResponseDTO>> getAllClassStudies() {
        List<ClassStudyResponseDTO> classStudies = classStudyService.getAllClassStudies();
        return new ResponseEntity<>(classStudies, HttpStatus.OK);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<ClassStudyResponseDTO> updateClassStudy(
            @PathVariable UUID classId,
            @RequestParam int year,
            @RequestParam BigDecimal totalGrade) {
        ClassStudyResponseDTO updatedClassStudy = classStudyService.updateClassStudy(classId, year, totalGrade);
        return new ResponseEntity<>(updatedClassStudy, HttpStatus.OK);
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Void> deleteClassStudy(@PathVariable UUID classId) {
        classStudyService.deleteClassStudy(classId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
