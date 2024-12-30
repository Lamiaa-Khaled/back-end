package com.ems.ems_app.repos;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.SuperResource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ResourceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(Resource resource) {
        entityManager.persist(resource);
    }

    @Transactional
    public void update(Resource resource) {
        entityManager.merge(resource);
    }

    public Resource findById(UUID id) {
        return entityManager.find(Resource.class, id);
    }

    @Transactional
    public void deleteById(UUID id) {
        Resource resource = findById(id);
        if (resource != null) {
            entityManager.remove(resource);
        }
    }

    public List<Resource> findAll() {
        return entityManager.createQuery("SELECT r FROM Resource r", Resource.class).getResultList();
    }
    public Resource findByName(String name) {
        try {
            return entityManager.createQuery(
                            "SELECT r FROM Resource r WHERE r.name = :name", Resource.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Return null if no resource is found
        }
    }

}

