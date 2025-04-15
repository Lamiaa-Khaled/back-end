package com.ems.ems_app.controllers;

<<<<<<< HEAD
import com.ems.ems_app.dto.requestDTO.ResourceRequestDTO;
import com.ems.ems_app.dto.responseDTO.ResourceResponseDTO;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.services.ResourceDirectoryService;
import com.ems.ems_app.services.ResourceService;
import com.ems.ems_app.services.SuperResourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final ResourceDirectoryService resourceDirectoryService;
    private final SuperResourceService superResourceService;

    @Autowired
    public ResourceController(ResourceService resourceService, ResourceDirectoryService resourceDirectoryService, SuperResourceService superResourceService) {
        this.resourceService = resourceService;
        this.resourceDirectoryService = resourceDirectoryService;
        this.superResourceService = superResourceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> getResourceById(@PathVariable UUID id) {
        ResourceResponseDTO resource = resourceService.getResourceById(id);
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {  // Changed return type
        List<ResourceResponseDTO> resources = resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    @PostMapping(consumes = "multipart/form-data")  // Specify consumes
    public ResponseEntity<Void> createResource(@Valid @ModelAttribute ResourceRequestDTO resourceRequestDTO) throws IOException { // Use @ModelAttribute
        resourceService.createResource(resourceRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResource(@PathVariable UUID id, @Valid @RequestBody ResourceRequestDTO resourceRequestDTO) {
        resourceService.updateResource(id, resourceRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
=======
import com.ems.ems_app.dto.ResourceDTO;
import com.ems.ems_app.entities.Resource;
import com.ems.ems_app.services.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public List<Resource> getAllResources() {
        return resourceService.getAllResources();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable UUID id) {
        return resourceService.getResourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Resource createResource(@RequestBody ResourceDTO resourceDTO) {
        Resource resource = new Resource();
        resource.setName(resourceDTO.getName());
        resource.setType(resourceDTO.getType());
       // resource.setResourceDirectory(resourceService.getResourceById(UUID.fromString(resourceDTO.getResourceDirectoryId())).orElseThrow());
        return resourceService.createOrUpdateResource(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(@PathVariable UUID id, @RequestBody ResourceDTO resourceDTO) {
        return resourceService.getResourceById(id)
                .map(existingResource -> {
                    existingResource.setName(resourceDTO.getName());
                    existingResource.setType(resourceDTO.getType());
                   // existingResource.setResourceDirectory(resourceService.getResourceById(UUID.fromString(resourceDTO.getResourceDirectoryId())).orElseThrow());
                    return ResponseEntity.ok(resourceService.createOrUpdateResource(existingResource));
                })
                .orElse(ResponseEntity.notFound().build());
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable UUID id) {
        resourceService.deleteResource(id);
<<<<<<< HEAD
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // SuperResource handling (data upload and retrieval)
    @PostMapping("/{resourceId}/data")
    public ResponseEntity<Void> uploadResourceData(@PathVariable UUID resourceId, @RequestParam("file") MultipartFile file) throws IOException {
        superResourceService.createSuperResource(resourceId, file.getBytes());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<byte[]> getResourceData(@PathVariable UUID id) {
        Optional<SuperResource> superResourceOptional = superResourceService.getSuperResourceById(id);
        if (superResourceOptional.isPresent()) {
            SuperResource superResource = superResourceOptional.get();
            return ResponseEntity.ok()
                    .body(superResource.getData()); // Assumes you want to return the raw bytes
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/data/{id}")
    public ResponseEntity<Void> updateResourceData(@PathVariable UUID id, @RequestParam("file") MultipartFile file) throws IOException {
        superResourceService.updateSuperResource(id, file.getBytes());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/data/{id}")
    public ResponseEntity<Void> deleteResourceData(@PathVariable UUID id) {
        superResourceService.deleteSuperResource(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
=======
        return ResponseEntity.noContent().build();
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }
}