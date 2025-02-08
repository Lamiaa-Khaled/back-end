package com.ems.ems_app.repos;


import com.ems.ems_app.entities.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdminRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Admin> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Admin.class, id));
    }

    public List<Admin> findAll() {
        return entityManager.createQuery("SELECT a FROM Admin a", Admin.class).getResultList();
    }

    @Transactional
    public Admin save(Admin admin) {
        if (admin.getAdminId() == null) {
            entityManager.persist(admin);
            return admin;
        } else {
            return entityManager.merge(admin);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Admin> admin = findById(id);
        admin.ifPresent(entityManager::remove);
    }
}