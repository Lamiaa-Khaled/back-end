package com.ems.ems_app.repos;


import com.ems.ems_app.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Student.class, id));
    }

    public List<Student> findAll() {
        return entityManager.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    @Transactional
    public Student save(Student student) {
        if (student.getStudentId() == null) {
            entityManager.persist(student);
            return student;
        } else {
            return entityManager.merge(student);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Student> student = findById(id);
        student.ifPresent(entityManager::remove);
    }
}