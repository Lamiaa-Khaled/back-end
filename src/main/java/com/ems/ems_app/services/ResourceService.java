package com.ems.ems_app.services;

import com.ems.ems_app.dto.ResourceDTO;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.repos.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public void createResource(ResourceDTO resourceDTO) {
        Resource resource = new Resource(
                resourceDTO.getName(),
                resourceDTO.getType(),
                resourceDTO.getResourceDirId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        resourceRepository.save(resource);
    }

    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(resource -> new ResourceDTO(resource.getName(), resource.getType(), resource.getResourceDirId()))
                .collect(Collectors.toList());
    }

    public ResourceDTO getResourceById(UUID id) {
        Resource resource = resourceRepository.findById(id);
        if (resource == null) {
            throw new RuntimeException("Resource not found");
        }
        return new ResourceDTO(resource.getName(), resource.getType(), resource.getResourceDirId());
    }

    public void updateResource(UUID id, ResourceDTO resourceDTO) {
        Resource resource = resourceRepository.findById(id);
        if (resource == null) {
            throw new RuntimeException("Resource not found");
        }
        resource.setName(resourceDTO.getName());
        resource.setType(resourceDTO.getType());
        resource.setResourceDirId(resourceDTO.getResourceDirId());
        resource.setUpdatedAt(LocalDateTime.now());
        resourceRepository.update(resource);
    }

    public void deleteResource(UUID id) {
        resourceRepository.deleteById(id);
    }
}

