package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.requestDTO.ResourceDirectoryRequestDTO;
import com.ems.ems_app.dto.responseDTO.DirectoryWithResourcesDTO;
import com.ems.ems_app.dto.responseDTO.ResourceDirectoryResponseDTO;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.services.ResourceDirectoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resource-directories")
public class ResourceDirectoryController {

    private final ResourceDirectoryService resourceDirectoryService;

    @Autowired
    public ResourceDirectoryController(ResourceDirectoryService resourceDirectoryService) {
        this.resourceDirectoryService = resourceDirectoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDirectoryResponseDTO> getResourceDirectoryById(@PathVariable UUID id) {
        ResourceDirectoryResponseDTO resourceDirectory = resourceDirectoryService.getResourceDirectoryById(id);
        return ResponseEntity.ok(resourceDirectory);
    }

    @GetMapping("/{id}/with-resources")
    public ResponseEntity<DirectoryWithResourcesDTO> getDirectoryWithResources(@PathVariable UUID id) {
        DirectoryWithResourcesDTO directoryWithResources = resourceDirectoryService.getDirectoryWithResources(id);
        return ResponseEntity.ok(directoryWithResources);
    }

    @GetMapping
    public ResponseEntity<List<ResourceDirectory>> getAllResourceDirectories() {
        List<ResourceDirectory> resourceDirectories = resourceDirectoryService.getAllResourceDirectories();
        return ResponseEntity.ok(resourceDirectories);
    }

    @PostMapping
    public ResponseEntity<Void> createResourceDirectory(@Valid @RequestBody ResourceDirectoryRequestDTO resourceDirectoryRequestDTO) {
        resourceDirectoryService.createResourceDirectory(resourceDirectoryRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateResourceDirectory(@PathVariable UUID id, @Valid @RequestBody ResourceDirectoryRequestDTO resourceDirectoryRequestDTO) {
        resourceDirectoryService.updateResourceDirectory(id, resourceDirectoryRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceDirectory(@PathVariable UUID id) {
        resourceDirectoryService.deleteResourceDirectory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
