package com.ems.ems_app.services;

<<<<<<< HEAD
import com.ems.ems_app.dto.requestDTO.CourseRequestDTO;
import com.ems.ems_app.dto.responseDTO.CourseResponseDTO;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.CourseRepository;
import com.ems.ems_app.repos.GroupRepository;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
=======
import com.ems.ems_app.dto.CourseDTO;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.repos.CourseRepository;
import com.ems.ems_app.repos.GroupRepository;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import org.springframework.stereotype.Service;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa

import java.util.List;
import java.util.Optional;
import java.util.UUID;
<<<<<<< HEAD
import java.util.stream.Collectors;

@Service
public class CourseService {

=======

@Service
public class CourseService {
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final ResourceDirectoryRepository resourceDirectoryRepository;

<<<<<<< HEAD
    @Autowired
    public CourseService(CourseRepository courseRepository, GroupRepository groupRepository, ResourceDirectoryRepository resourceDirectoryRepository) {
=======
    public CourseService(CourseRepository courseRepository, GroupRepository groupRepository,
                         ResourceDirectoryRepository resourceDirectoryRepository) {
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.resourceDirectoryRepository = resourceDirectoryRepository;
    }

<<<<<<< HEAD
    public CourseResponseDTO getCourseByCode(String code) {
        Optional<Course> courseOptional = courseRepository.findById(code);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            // TODO: Retrieve avatar data from storage (e.g., database, file system, cloud)
            byte[] avatar = null;  // Replace with actual avatar retrieval logic
            String avatarType = null; // Replace with actual avatar type retrieval logic
            return CourseResponseDTO.fromEntity(course, avatar, avatarType);
        } else {
            throw new ResourceNotFoundException("Course not found with code: " + code);
        }
    }

    public List<CourseResponseDTO> getAllCourses() {  // Return List of DTOs
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .map(course -> {
                    byte[] avatar = null; // Retrieve avatar data
                    String avatarType = null;
                    return CourseResponseDTO.fromEntity(course, avatar, avatarType);
                })
                .collect(Collectors.toList());
    }
@Transactional
    public void createCourse(CourseRequestDTO courseRequestDTO) {
        // Validate Group and ResourceDirectory exist
        Group group = groupRepository.findById(courseRequestDTO.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + courseRequestDTO.getGroupId()));

        ResourceDirectory baseDirectory = resourceDirectoryRepository.findById(courseRequestDTO.getBaseDirectoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + courseRequestDTO.getBaseDirectoryId()));

        Course course = Course.builder()
                .code(courseRequestDTO.getCode())
                .name(courseRequestDTO.getName())
                .active(courseRequestDTO.isActive())
                .group(group)
                .baseDirectory(baseDirectory)
                .build();
        // TODO: Store avatar data
        courseRepository.save(course);
    }
@Transactional
    public void updateCourse(String code, CourseRequestDTO courseRequestDTO) {
        Course course = courseRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + code));

        Group group = groupRepository.findById(courseRequestDTO.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + courseRequestDTO.getGroupId()));

        ResourceDirectory baseDirectory = resourceDirectoryRepository.findById(courseRequestDTO.getBaseDirectoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + courseRequestDTO.getBaseDirectoryId()));

        course.setName(courseRequestDTO.getName());
        course.setActive(courseRequestDTO.isActive());
        course.setGroup(group);
        course.setBaseDirectory(baseDirectory);

        courseRepository.update(course);
    }

    public void deleteCourse(String code) {
        courseRepository.deleteById(code);
    }
}
=======
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
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
