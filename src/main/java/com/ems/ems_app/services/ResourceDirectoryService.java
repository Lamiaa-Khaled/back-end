package com.ems.ems_app.services;

import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResourceDirectoryService {
    private final ResourceDirectoryRepository resourceDirectoryRepository;

    public ResourceDirectoryService(ResourceDirectoryRepository resourceDirectoryRepository) {
        this.resourceDirectoryRepository = resourceDirectoryRepository;
    }

    public List<ResourceDirectory> getAllResourceDirectories() {
        return resourceDirectoryRepository.findAll();
    }

    public Optional<ResourceDirectory> getResourceDirectoryById(UUID id) {
        return resourceDirectoryRepository.findById(id);
    }

    public ResourceDirectory createOrUpdateResourceDirectory(ResourceDirectory resourceDirectory) {
        return resourceDirectoryRepository.save(resourceDirectory);
    }

    public void deleteResourceDirectory(UUID id) {
        resourceDirectoryRepository.deleteById(id);
    }
}
