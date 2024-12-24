package com.ems.ems_app.repos;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.entities.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Group> findAll() {
        return entityManager.createQuery("FROM Group", Group.class).getResultList();
    }

    public Optional<Group> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Group.class, id));
    }

    @Transactional
    public Group save(Group group) {
        if (group.getId() == null) {
            group.setId(UUID.randomUUID());
            entityManager.persist(group);
        } else {
            entityManager.merge(group);
        }
        return group;
    }

    @Transactional
    public void deleteById(UUID id) {
        Group group = entityManager.find(Group.class, id);
        if (group != null) {
            entityManager.remove(group);
        }
    }
}
