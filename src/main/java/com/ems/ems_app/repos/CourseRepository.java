package com.ems.ems_app.repos;

import com.ems.ems_app.entities.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional

    public void save(Course course) {
        entityManager.persist(course);
    }

    public Course findById(String code) {
        return entityManager.find(Course.class, code);
    }

    public List<Course> findAll() {
        return entityManager.createQuery("SELECT c FROM Course c", Course.class).getResultList();
    }
    @Transactional

    public void update(Course course) {
        entityManager.merge(course);
    }
    @Transactional

    public void deleteById(String code) {
        Course course = findById(code);
        if (course != null) {
            entityManager.remove(course);
        }
    }
}

