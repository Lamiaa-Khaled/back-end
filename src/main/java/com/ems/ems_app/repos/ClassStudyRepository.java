package com.ems.ems_app.repos;


import com.ems.ems_app.entities.ClassStudy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClassStudyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<ClassStudy> findById(Long id) {
        return Optional.ofNullable(entityManager.find(ClassStudy.class, id));
    }

    public List<ClassStudy> findAll() {
        return entityManager.createQuery("SELECT c FROM ClassStudy c", ClassStudy.class).getResultList();
    }

    @Transactional
    public ClassStudy save(ClassStudy classStudy) {
        if (classStudy.getClassId() == null) {
            entityManager.persist(classStudy);
            return classStudy;
        } else {
            return entityManager.merge(classStudy);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<ClassStudy> classStudy = findById(id);
        classStudy.ifPresent(entityManager::remove);
    }
}