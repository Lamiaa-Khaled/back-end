package com.ems.ems_app.services;

import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.repos.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public Optional<Resource> getResourceById(UUID id) {
        return resourceRepository.findById(id);
    }

    public Resource createOrUpdateResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public void deleteResource(UUID id) {
        resourceRepository.deleteById(id);
    }
}
