package com.ems.ems_app.services;

<<<<<<< HEAD
import com.ems.ems_app.dto.requestDTO.ResourceDirectoryRequestDTO;
import com.ems.ems_app.dto.responseDTO.DirectoryWithResourcesDTO;
import com.ems.ems_app.dto.responseDTO.ResourceDirectoryResponseDTO;
import com.ems.ems_app.dto.responseDTO.ResourceResponseDTO;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import com.ems.ems_app.repos.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceDirectoryService {

    private final ResourceDirectoryRepository resourceDirectoryRepository;
    private final ResourceRepository resourceRepository; // To fetch resources within a directory

    @Autowired
    public ResourceDirectoryService(ResourceDirectoryRepository resourceDirectoryRepository, ResourceRepository resourceRepository) {
        this.resourceDirectoryRepository = resourceDirectoryRepository;
        this.resourceRepository = resourceRepository;
    }

    public ResourceDirectoryResponseDTO getResourceDirectoryById(UUID id) {
        ResourceDirectory resourceDirectory = resourceDirectoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + id));
        return ResourceDirectoryResponseDTO.fromEntity(resourceDirectory);
    }

    public DirectoryWithResourcesDTO getDirectoryWithResources(UUID id) {
        ResourceDirectory directory = resourceDirectoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + id));

        List<Resource> resources = resourceRepository.findAll().stream() // Assuming findAll() gets all resources. Refine if needed.
                .filter(resource -> resource.getResourceDirectory().getId().equals(id))
                .collect(Collectors.toList());

        List<ResourceResponseDTO> resourceDTOs = resources.stream()
                .map(ResourceResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return DirectoryWithResourcesDTO.fromEntity(directory, resourceDTOs);
    }


=======
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

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    public List<ResourceDirectory> getAllResourceDirectories() {
        return resourceDirectoryRepository.findAll();
    }

<<<<<<< HEAD
    public void createResourceDirectory(ResourceDirectoryRequestDTO resourceDirectoryRequestDTO) {
        ResourceDirectory resourceDirectory = new ResourceDirectory();
        resourceDirectory.setId(UUID.randomUUID());
        resourceDirectory.setName(resourceDirectoryRequestDTO.getName());
        resourceDirectory.setCreator(resourceDirectoryRequestDTO.getCreator());
        resourceDirectory.setBaseDirId(resourceDirectoryRequestDTO.getBaseDirId());
        resourceDirectory.setCreatedAt(LocalDateTime.now());
        resourceDirectory.setUpdatedAt(LocalDateTime.now());
        resourceDirectoryRepository.save(resourceDirectory);
    }

    public void updateResourceDirectory(UUID id, ResourceDirectoryRequestDTO resourceDirectoryRequestDTO) {
        ResourceDirectory resourceDirectory = resourceDirectoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + id));

        resourceDirectory.setName(resourceDirectoryRequestDTO.getName());
        resourceDirectory.setCreator(resourceDirectoryRequestDTO.getCreator());
        resourceDirectory.setBaseDirId(resourceDirectoryRequestDTO.getBaseDirId());
        resourceDirectory.setUpdatedAt(LocalDateTime.now());
        resourceDirectoryRepository.update(resourceDirectory);
=======
    public Optional<ResourceDirectory> getResourceDirectoryById(UUID id) {
        return resourceDirectoryRepository.findById(id);
    }

    public ResourceDirectory createOrUpdateResourceDirectory(ResourceDirectory resourceDirectory) {
        return resourceDirectoryRepository.save(resourceDirectory);
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    public void deleteResourceDirectory(UUID id) {
        resourceDirectoryRepository.deleteById(id);
    }
}
