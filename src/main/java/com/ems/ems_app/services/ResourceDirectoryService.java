package com.ems.ems_app.services;

import com.ems.ems_app.dto.ResourceDirectoryDTO;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResourceDirectoryService {

    private final ResourceDirectoryRepository repository;

    public ResourceDirectoryService(ResourceDirectoryRepository repository) {
        this.repository = repository;
    }

    public void createResourceDirectory(ResourceDirectoryDTO dto) {
        ResourceDirectory resourceDirectory = new ResourceDirectory();
        resourceDirectory.setName(dto.getName());
        resourceDirectory.setCreator(dto.getCreator());
        repository.save(resourceDirectory);
    }

    public ResourceDirectory getResourceDirectoryById(UUID id) {
        return repository.findById(id);
    }

    public List<ResourceDirectory> getAllResourceDirectories() {
        return repository.findAll();
    }
@Transactional
    public void updateResourceDirectory(UUID id, ResourceDirectoryDTO dto) {
        ResourceDirectory resourceDirectory = repository.findById(id);
        if (resourceDirectory != null) {
            resourceDirectory.setName(dto.getName());
            resourceDirectory.setCreator(dto.getCreator());
            repository.update(resourceDirectory);
        }
    }
//@Transactional
    public void deleteResourceDirectory(UUID id) {
        repository.deleteById(id);
    }
}
