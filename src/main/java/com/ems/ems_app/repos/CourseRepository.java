package com.ems.ems_app.repos;

import com.ems.ems_app.entities.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Course> findAll() {
        return entityManager.createQuery("FROM Course", Course.class).getResultList();
    }

    public Optional<Course> findById(String code) {
        return Optional.ofNullable(entityManager.find(Course.class, code));
    }

    @Transactional
    public Course save(Course course) {
        if (course.getCode() == null) {
            throw new IllegalArgumentException("Course code cannot be null");
        }
        if (entityManager.find(Course.class, course.getCode()) == null) {
            entityManager.persist(course);
        } else {
            entityManager.merge(course);
        }
        return course;
    }

    @Transactional
    public void deleteById(String code) {
        Course course = entityManager.find(Course.class, code);
        if (course != null) {
            entityManager.remove(course);
        }
    }
}
