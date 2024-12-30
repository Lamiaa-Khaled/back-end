package com.ems.ems_app.services;
import com.ems.ems_app.dto.CourseDTO;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.repos.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    @Transactional

    public void createCourse(CourseDTO courseDto, Resource avatar, Group group, ResourceDirectory resourceDirectory) {
        Course course = new Course();
        course.setCode(courseDto.getName()); // Assuming `code` is derived from `name`
        course.setName(courseDto.getName());
        course.setAvatar(avatar);
        course.setGroup(group);
        course.setResourceDirectory(resourceDirectory);
        courseRepository.save(course);
    }

    public Course getCourseByCode(String code) {
        return courseRepository.findById(code);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        System.out.println("Retrieved courses: " + courses);
        return courses;
    }
    @Transactional

    public void updateCourse(String code, CourseDTO courseDto, Resource avatar, Group group, ResourceDirectory resourceDirectory) {
        Course course = courseRepository.findById(code);
        if (course != null) {
            course.setName(courseDto.getName());
            course.setAvatar(avatar);
            course.setGroup(group);
            course.setResourceDirectory(resourceDirectory);
            courseRepository.update(course);
        }
    }
@Transactional
    public void deleteCourse(String code) {
        courseRepository.deleteById(code);
    }
}


