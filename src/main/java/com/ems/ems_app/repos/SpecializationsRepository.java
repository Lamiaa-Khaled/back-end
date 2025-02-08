package com.ems.ems_app.repos;

import com.ems.ems_app.entities.Specializations;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpecializationsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Specializations> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Specializations.class, id));
    }

    public List<Specializations> findAll() {
        return entityManager.createQuery("SELECT s FROM Specializations s", Specializations.class).getResultList();
    }

    @Transactional
    public Specializations save(Specializations specializations) {
        if (specializations.getSpecializationId() == null) {
            entityManager.persist(specializations);
            return specializations;
        } else {
            return entityManager.merge(specializations);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Specializations> specializations = findById(id);
        specializations.ifPresent(entityManager::remove);
    }
}