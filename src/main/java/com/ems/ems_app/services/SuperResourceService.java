package com.ems.ems_app.services;

import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.ResourceRepository;
import com.ems.ems_app.repos.SuperResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SuperResourceService {

    private final SuperResourceRepository superResourceRepository;
    private final ResourceRepository resourceRepository;

    @Autowired
    public SuperResourceService(SuperResourceRepository superResourceRepository, ResourceRepository resourceRepository) {
        this.superResourceRepository = superResourceRepository;
        this.resourceRepository = resourceRepository;
    }

    public Optional<SuperResource> getSuperResourceById(UUID id) {
        return superResourceRepository.findById(id);
    }

    public void createSuperResource(UUID resourceId, byte[] data) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + resourceId));

        SuperResource superResource = new SuperResource();
        superResource.setId(UUID.randomUUID());
        superResource.setData(data);
        superResource.setResource(resource);

        superResourceRepository.save(superResource);
    }

    public void updateSuperResource(UUID id, byte[] data) {
        SuperResource superResource = superResourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SuperResource not found with id: " + id));

        superResource.setData(data);
        superResourceRepository.update(superResource);
    }

    public void deleteSuperResource(UUID id) {
        superResourceRepository.deleteById(id);
    }
}