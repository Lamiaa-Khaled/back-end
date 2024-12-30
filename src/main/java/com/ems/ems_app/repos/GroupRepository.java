package com.ems.ems_app.repos;

import com.ems.ems_app.entities.Group;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupRepository  {

   @PersistenceContext
    private EntityManager entityManager;

    // Save or Update
    public Group save(Group group) {
        if (group.getId() == null) {
            group.setId(UUID.randomUUID());
            entityManager.persist(group);
        } else {
            entityManager.merge(group);
        }
        return group;
    }

    // Find by ID
    public Optional<Group> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Group.class, id));
    }
    @Transactional
    // Delete by ID
    public void deleteById(UUID id) {
        Group group = findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        entityManager.remove(group);
    }

    // Find all
    public List<Group> findAll() {
        return entityManager.createQuery("SELECT g FROM Group g", Group.class).getResultList();
    }
    @Transactional
    public Group updateGroup(UUID id, Group group) {
        // Find the existing group
        Group existingGroup = entityManager.find(Group.class, id);
        if (existingGroup != null) {
            // Update fields manually or merge the group
            existingGroup.setName(group.getName());
            existingGroup.setDescription(group.getDescription());
            return entityManager.merge(existingGroup); // Merge to update
        }
        return null;  // Return null if not found
    }

    @Transactional
    public void deleteAllGroups() {
        // Custom query to delete all records
        String query = "DELETE FROM Group g";
        Query deleteQuery = entityManager.createQuery(query);
        deleteQuery.executeUpdate();
    }

}
