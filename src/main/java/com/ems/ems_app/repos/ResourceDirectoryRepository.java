package com.ems.ems_app.repos;

import com.ems.ems_app.entities.ResourceDirectory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ResourceDirectoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ResourceDirectory resourceDirectory) {
        entityManager.persist(resourceDirectory);
    }

    public ResourceDirectory findById(UUID id) {
        return entityManager.find(ResourceDirectory.class, id);
    }

    public List<ResourceDirectory> findAll() {
        return entityManager.createQuery("SELECT r FROM ResourceDirectory r", ResourceDirectory.class).getResultList();
    }

    public void update(ResourceDirectory resourceDirectory) {
        entityManager.merge(resourceDirectory);
    }

    public void deleteById(UUID id) {
        ResourceDirectory resourceDirectory = findById(id);
        if (resourceDirectory != null) {
            entityManager.remove(resourceDirectory);
        }
    }
}

