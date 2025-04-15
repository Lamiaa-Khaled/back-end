package com.ems.ems_app.controllers;

<<<<<<< HEAD
import com.ems.ems_app.dto.requestDTO.ResourceDirectoryRequestDTO;
import com.ems.ems_app.dto.responseDTO.DirectoryWithResourcesDTO;
import com.ems.ems_app.dto.responseDTO.ResourceDirectoryResponseDTO;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.services.ResourceDirectoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
=======
import com.ems.ems_app.dto.ResourceDirectoryDTO;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.services.ResourceDirectoryService;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
<<<<<<< HEAD
@RequestMapping("/resource-directories")
public class ResourceDirectoryController {

    private final ResourceDirectoryService resourceDirectoryService;

    @Autowired
=======
@RequestMapping("/api/resource-directories")
public class ResourceDirectoryController {
    private final ResourceDirectoryService resourceDirectoryService;

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    public ResourceDirectoryController(ResourceDirectoryService resourceDirectoryService) {
        this.resourceDirectoryService = resourceDirectoryService;
    }

<<<<<<< HEAD
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
=======
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
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResourceDirectory(@PathVariable UUID id) {
        resourceDirectoryService.deleteResourceDirectory(id);
<<<<<<< HEAD
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
=======
        return ResponseEntity.noContent().build();
    }
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
