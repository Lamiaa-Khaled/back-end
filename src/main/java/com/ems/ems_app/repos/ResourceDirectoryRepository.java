package com.ems.ems_app.repos;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.ResourceDirectory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ResourceDirectoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ResourceDirectory> findAll() {
        return entityManager.createQuery("FROM ResourceDirectory", ResourceDirectory.class).getResultList();
    }

    public Optional<ResourceDirectory> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(ResourceDirectory.class, id));
    }

    @Transactional
    public ResourceDirectory save(ResourceDirectory resourceDirectory) {
        if (resourceDirectory.getId() == null) {
            resourceDirectory.setId(UUID.randomUUID());
            entityManager.persist(resourceDirectory);
        } else {
            entityManager.merge(resourceDirectory);
        }
        return resourceDirectory;
    }

    @Transactional
    public void deleteById(UUID id) {
        ResourceDirectory resourceDirectory = entityManager.find(ResourceDirectory.class, id);
        if (resourceDirectory != null) {
            entityManager.remove(resourceDirectory);
        }
    }
}
