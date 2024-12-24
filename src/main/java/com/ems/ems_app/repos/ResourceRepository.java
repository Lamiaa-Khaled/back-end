package com.ems.ems_app.repos;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ResourceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Resource> findAll() {
        return entityManager.createQuery("FROM Resource", Resource.class).getResultList();
    }

    public Optional<Resource> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Resource.class, id));
    }

    @Transactional
    public Resource save(Resource resource) {
        if (resource.getId() == null) {
            resource.setId(UUID.randomUUID());
            entityManager.persist(resource);
        } else {
            entityManager.merge(resource);
        }
        return resource;
    }

    @Transactional
    public void deleteById(UUID id) {
        Resource resource = entityManager.find(Resource.class, id);
        if (resource != null) {
            entityManager.remove(resource);
        }
    }
}
