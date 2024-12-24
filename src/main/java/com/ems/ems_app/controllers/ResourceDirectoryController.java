package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.ResourceDirectoryDTO;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.services.ResourceDirectoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resource-directories")
public class ResourceDirectoryController {
    private final ResourceDirectoryService resourceDirectoryService;

    public ResourceDirectoryController(ResourceDirectoryService resourceDirectoryService) {
        this.resourceDirectoryService = resourceDirectoryService;
    }

    @GetMapping
    public List<ResourceDirectory> getAllResourceDirectories() {
        return resourceDirectoryService.getAllResourceDirectories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDirectory> getResourceDirectoryById(@PathVariable UUID id) {
        return resourceDirectoryService.getResourceDirectoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResourceDirectory createResourceDirectory(@RequestBody ResourceDirectoryDTO resourceDirectoryDTO) {
        ResourceDirectory resourceDirectory = new ResourceDirectory();
        resourceDirectory.setName(resourceDirectoryDTO.getName());
        resourceDirectory.setCreator(resourceDirectoryDTO.getCreator());
        return resourceDirectoryService.createOrUpdateResourceDirectory(resourceDirectory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceDirectory> updateResourceDirectory(@PathVariable UUID id, @RequestBody ResourceDirectoryDTO resourceDirectoryDTO) {
        return resourceDirectoryService.getResourceDirectoryById(id)
                .map(existingDirectory -> {
                    existingDirectory.setName(resourceDirectoryDTO.getName());
                    existingDirectory.setCreator(resourceDirectoryDTO.getCreator());
                    return ResponseEntity.ok(resourceDirectoryService.createOrUpdateResourceDirectory(existingDirectory));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceDirectory(@PathVariable UUID id) {
        resourceDirectoryService.deleteResourceDirectory(id);
        return ResponseEntity.noContent().build();
    }
}