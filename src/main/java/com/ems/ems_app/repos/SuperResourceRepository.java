package com.ems.ems_app.repos;
import com.ems.ems_app.entities.SuperResource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public class SuperResourceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public SuperResource save(SuperResource superResource) {
        entityManager.persist(superResource);
        return superResource;
    }

    public SuperResource findById(UUID id) {
        return entityManager.find(SuperResource.class, id);
    }

    public List<SuperResource> findAll() {
        return entityManager.createQuery("FROM SuperResource", SuperResource.class).getResultList();
    }
    // Create a new SuperResource
    public SuperResource createSuperResource(SuperResource superResource) {
        entityManager.persist(superResource);
        return superResource;
    }



    public SuperResource updateSuperResource(SuperResource superResource) {
        return entityManager.merge(superResource);
    }

    public void deleteById(UUID id) {
        SuperResource superResource = findById(id);
        if (superResource != null) {
            entityManager.remove(superResource);
        }
    }
}

