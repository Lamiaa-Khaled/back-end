package com.ems.ems_app.services;

import com.ems.ems_app.dto.SuperResourceDTO;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.repos.ResourceRepository;
import com.ems.ems_app.repos.SuperResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuperResourceService {

    private final SuperResourceRepository superResourceRepository;
    private final ResourceRepository resourceRepository;

    public SuperResourceService(SuperResourceRepository superResourceRepository, ResourceRepository resourceRepository) {
        this.superResourceRepository = superResourceRepository;
        this.resourceRepository = resourceRepository;
    }

    public SuperResourceDTO createSuperResource(SuperResourceDTO dto) {
        Resource resource = resourceRepository.findByName(dto.getResourceName());
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found");
        }


        SuperResource superResource = new SuperResource(dto.getData(), resource);
        SuperResource savedSuperResource = superResourceRepository.createSuperResource(superResource);

        return new SuperResourceDTO(savedSuperResource.getData(), resource.getName());
    }


    public List<SuperResourceDTO> getAllSuperResources() {
        return superResourceRepository.findAll().stream()
                .map(sr -> new SuperResourceDTO(sr.getData(), sr.getResource().getName()))
                .collect(Collectors.toList());
    }

    public void deleteSuperResource(UUID id) {
        superResourceRepository.deleteById(id);
    }
    public SuperResourceDTO updateSuperResource(UUID id, SuperResourceDTO dto) {
        // Find the existing SuperResource by ID
        SuperResource existingSuperResource = superResourceRepository.findById(id);
        if (existingSuperResource == null) {
            throw new IllegalArgumentException("SuperResource with ID '" + id + "' not found");
        }

        // Find the associated Resource by name (if provided)
        if (dto.getResourceName() != null) {
            Resource resource = resourceRepository.findByName(dto.getResourceName());
            if (resource == null) {
                throw new IllegalArgumentException("Resource with name '" + dto.getResourceName() + "' not found");
            }
            existingSuperResource.setResource(resource);
        }

        // Update the data field
        if (dto.getData() != null) {
            existingSuperResource.setData(dto.getData());
        }

        // Save the updated entity
        SuperResource updatedSuperResource = superResourceRepository.updateSuperResource(existingSuperResource);

        // Return the updated DTO
        return new SuperResourceDTO(updatedSuperResource.getData(), updatedSuperResource.getResource().getName());
    }
}
