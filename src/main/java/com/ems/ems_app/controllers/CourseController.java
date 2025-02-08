package com.ems.ems_app.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ems.ems_app.dto.requestDTO.CourseRequestDTO;
import com.ems.ems_app.dto.responseDTO.CourseResponseDTO;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);


    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<CourseResponseDTO> getCourseByCode(@PathVariable String code) {
        CourseResponseDTO course = courseService.getCourseByCode(code);
        return ResponseEntity.ok(course);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        List<CourseResponseDTO> courses = courseService.getAllCourses();  // Get DTOs
        return ResponseEntity.ok(courses);
    }

    @PostMapping("create")
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) {

        logger.info("Received CourseRequestDTO: {}", courseRequestDTO);  // Log the DTO
        courseService.createCourse(courseRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    public ResponseEntity<Void> updateCourse(@PathVariable String code, @Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        courseService.updateCourse(code, courseRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String code) {
        courseService.deleteCourse(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}