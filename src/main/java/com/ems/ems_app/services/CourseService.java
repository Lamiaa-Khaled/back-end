package com.ems.ems_app.services;

import com.ems.ems_app.dto.CourseDTO;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.repos.CourseRepository;
import com.ems.ems_app.repos.GroupRepository;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final ResourceDirectoryRepository resourceDirectoryRepository;

    public CourseService(CourseRepository courseRepository, GroupRepository groupRepository,
                         ResourceDirectoryRepository resourceDirectoryRepository) {
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.resourceDirectoryRepository = resourceDirectoryRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseByCode(String code) {
        return courseRepository.findById(code);
    }

    public Course createCourse(CourseDTO courseDTO) {
        Group group = groupRepository.findById(UUID.fromString(courseDTO.getGroupName())).orElseThrow();
        ResourceDirectory resourceDirectory = resourceDirectoryRepository.findById(UUID.fromString(courseDTO.getResourceDirectoryName())).orElseThrow();

        Course course = new Course();
        course.setCode(courseDTO.getCode());
        course.setName(courseDTO.getName());
        course.setAvatarId(courseDTO.getAvatarId());
        course.setGroup(group);
        course.setResourceDirectory(resourceDirectory);
        return courseRepository.save(course);
    }
}
