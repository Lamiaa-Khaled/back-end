package com.ems.ems_app.services;

<<<<<<< HEAD
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.ResourceRepository;
import com.ems.ems_app.repos.SuperResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

=======
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.repos.SuperResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import java.util.Optional;
import java.util.UUID;

@Service
public class SuperResourceService {
<<<<<<< HEAD

    private final SuperResourceRepository superResourceRepository;
    private final ResourceRepository resourceRepository;

    @Autowired
    public SuperResourceService(SuperResourceRepository superResourceRepository, ResourceRepository resourceRepository) {
        this.superResourceRepository = superResourceRepository;
        this.resourceRepository = resourceRepository;
=======
    private final SuperResourceRepository superResourceRepository;

    public SuperResourceService(SuperResourceRepository superResourceRepository) {
        this.superResourceRepository = superResourceRepository;
    }

    public List<SuperResource> getAllSuperResources() {
        return superResourceRepository.findAll();
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    public Optional<SuperResource> getSuperResourceById(UUID id) {
        return superResourceRepository.findById(id);
    }

<<<<<<< HEAD
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
=======
    public SuperResource createOrUpdateSuperResource(SuperResource superResource) {
        return superResourceRepository.save(superResource);
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    public void deleteSuperResource(UUID id) {
        superResourceRepository.deleteById(id);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
