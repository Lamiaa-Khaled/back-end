package com.ems.ems_app.services;

<<<<<<< HEAD
import com.ems.ems_app.dto.requestDTO.ResourceDirectoryRequestDTO;
import com.ems.ems_app.dto.requestDTO.ResourceRequestDTO;
import com.ems.ems_app.dto.responseDTO.DirectoryWithResourcesDTO;
import com.ems.ems_app.dto.responseDTO.ResourceDirectoryResponseDTO;
import com.ems.ems_app.dto.responseDTO.ResourceResponseDTO;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.ResourceDirectoryRepository;
import com.ems.ems_app.repos.ResourceRepository;
import com.ems.ems_app.repos.SuperResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceDirectoryRepository resourceDirectoryRepository;
    private final SuperResourceRepository superResourceRepository;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ResourceDirectoryRepository resourceDirectoryRepository, SuperResourceRepository superResourceRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceDirectoryRepository = resourceDirectoryRepository;
        this.superResourceRepository = superResourceRepository;
    }

    public ResourceResponseDTO getResourceById(UUID id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        return ResourceResponseDTO.fromEntity(resource);
    }

    public List<ResourceResponseDTO> getAllResources() {
        List<Resource> resources = resourceRepository.findAll();
        return resources.stream()
                .map(ResourceResponseDTO::fromEntity)  // Use the fromEntity() method
                .collect(Collectors.toList());
    }



    @Transactional // VERY IMPORTANT
    public void createResource(ResourceRequestDTO resourceRequestDTO) throws IOException { // Add throws IOException
        ResourceDirectory resourceDirectory = resourceDirectoryRepository.findById(resourceRequestDTO.getResourceDirId())
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + resourceRequestDTO.getResourceDirId()));

        Resource resource = new Resource();
        resource.setId(UUID.randomUUID());
        resource.setName(resourceRequestDTO.getName());
        resource.setType(resourceRequestDTO.getType());
        resource.setResourceDirectory(resourceDirectory);
        resource.setCreatedAt(LocalDateTime.now());
        resource.setUpdatedAt(LocalDateTime.now());
        resourceRepository.save(resource);

        // Handle the file upload
        MultipartFile file = resourceRequestDTO.getFile();
        if (file != null && !file.isEmpty()) {
            SuperResource superResource = new SuperResource();
            superResource.setId(UUID.randomUUID());
            superResource.setData(file.getBytes());
            superResource.setResource(resource);
            superResourceRepository.save(superResource);
        }
    }

    public void updateResource(UUID id, ResourceRequestDTO resourceRequestDTO) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        ResourceDirectory resourceDirectory = resourceDirectoryRepository.findById(resourceRequestDTO.getResourceDirId())
                .orElseThrow(() -> new ResourceNotFoundException("ResourceDirectory not found with id: " + resourceRequestDTO.getResourceDirId()));

        resource.setName(resourceRequestDTO.getName());
        resource.setType(resourceRequestDTO.getType());
        resource.setResourceDirectory(resourceDirectory);
        resource.setUpdatedAt(LocalDateTime.now());
        resourceRepository.update(resource);
=======
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
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    public void deleteResource(UUID id) {
        resourceRepository.deleteById(id);
    }
}
<<<<<<< HEAD

=======
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
