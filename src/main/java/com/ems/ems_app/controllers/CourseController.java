package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.CourseDTO;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.services.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("create")
    public String createCourse(@RequestBody CourseDTO courseDto) {
        // Fetch related entities
        Resource avatar = new Resource(); // Replace with actual fetch logic
        Group group = new Group(); // Replace with actual fetch logic
        ResourceDirectory resourceDirectory = new ResourceDirectory(); // Replace with actual fetch logic

        courseService.createCourse(courseDto, avatar, group, resourceDirectory);
        return "Course created successfully!";
    }

    @GetMapping("/find {code}")
    public Course getCourse(@PathVariable String code) {
        return courseService.getCourseByCode(code);
    }

    @GetMapping("findAll")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PutMapping("/update {code}")
    public String updateCourse(@PathVariable String code, @RequestBody CourseDTO courseDto) {
        // Fetch related entities
        Resource avatar = new Resource(); // Replace with actual fetch logic
        Group group = new Group(); // Replace with actual fetch logic
        ResourceDirectory resourceDirectory = new ResourceDirectory(); // Replace with actual fetch logic

        courseService.updateCourse(code, courseDto, avatar, group, resourceDirectory);
        return "Course updated successfully!";
    }

    @DeleteMapping("/delete {code}")
    public String deleteCourse(@PathVariable String code) {
        courseService.deleteCourse(code);
        return "Course deleted successfully!";
    }
//    @GetMapping
//    public List<Course> findAll() {
//        return courseService.getAllCourses();
//    }

}

