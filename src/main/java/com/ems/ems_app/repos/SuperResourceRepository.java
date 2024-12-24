package com.ems.ems_app.repos;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.SuperResource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SuperResourceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<SuperResource> findAll() {
        return entityManager.createQuery("FROM SuperResource", SuperResource.class).getResultList();
    }

    public Optional<SuperResource> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(SuperResource.class, id));
    }

    @Transactional
    public SuperResource save(SuperResource superResource) {
        if (superResource.getId() == null) {
            superResource.setId(UUID.randomUUID());
            entityManager.persist(superResource);
        } else {
            entityManager.merge(superResource);
        }
        return superResource;
    }

    @Transactional
    public void deleteById(UUID id) {
        SuperResource superResource = entityManager.find(SuperResource.class, id);
        if (superResource != null) {
            entityManager.remove(superResource);
        }
    }
}
