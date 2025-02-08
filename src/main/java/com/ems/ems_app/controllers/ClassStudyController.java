package com.ems.ems_app.controllers;


import com.ems.ems_app.dto.ClassStudyDTO;
import com.ems.ems_app.entities.ClassStudy;
import com.ems.ems_app.services.ClassStudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("class-studies")
public class ClassStudyController {

    @Autowired
    private ClassStudyService classStudyService;

    @GetMapping("/{id}")
    public ResponseEntity<ClassStudyDTO> getClassStudyById(@PathVariable Long id) {
        ClassStudyDTO classStudyDTO = classStudyService.getClassStudyById(id);
        return ResponseEntity.ok(classStudyDTO);
    }

    @GetMapping("findAll")
    public List<ClassStudyDTO> getAllClassStudies() {
        List<ClassStudyDTO> classStudies = classStudyService.getAllClassStudies();
        return classStudies;
    }

    @PostMapping
    public ResponseEntity<ClassStudyDTO> createClassStudy(@RequestBody ClassStudy classStudy) {
        ClassStudyDTO createdClassStudy = classStudyService.createClassStudy(classStudy);
        return new ResponseEntity<>(createdClassStudy, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassStudyDTO> updateClassStudy(@PathVariable Long id, @RequestBody ClassStudy classStudyDetails) {
        ClassStudyDTO updatedClassStudy = classStudyService.updateClassStudy(id, classStudyDetails);
        return ResponseEntity.ok(updatedClassStudy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassStudy(@PathVariable Long id) {
        classStudyService.deleteClassStudy(id);
        return ResponseEntity.noContent().build();
    }
}