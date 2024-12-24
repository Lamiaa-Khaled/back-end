package com.ems.ems_app.controllers;

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
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable UUID id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}