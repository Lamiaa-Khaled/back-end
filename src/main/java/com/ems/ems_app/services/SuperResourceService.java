package com.ems.ems_app.services;

import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.repos.SuperResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SuperResourceService {
    private final SuperResourceRepository superResourceRepository;

    public SuperResourceService(SuperResourceRepository superResourceRepository) {
        this.superResourceRepository = superResourceRepository;
    }

    public List<SuperResource> getAllSuperResources() {
        return superResourceRepository.findAll();
    }

    public Optional<SuperResource> getSuperResourceById(UUID id) {
        return superResourceRepository.findById(id);
    }

    public SuperResource createOrUpdateSuperResource(SuperResource superResource) {
        return superResourceRepository.save(superResource);
    }

    public void deleteSuperResource(UUID id) {
        superResourceRepository.deleteById(id);
    }
}
