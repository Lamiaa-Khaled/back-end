package com.ems.ems_app.controllers;
import com.ems.ems_app.dto.ResourceDirectoryDTO;
import com.ems.ems_app.entities.ResourceDirectory;
import com.ems.ems_app.services.ResourceDirectoryService;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resource-directories")
public class ResourceDirectoryController {

    private final ResourceDirectoryService service;

    public ResourceDirectoryController(ResourceDirectoryService service) {
        this.service = service;
    }

    @PostMapping("create")
    public ResponseEntity<String> createResourceDirectory(@RequestBody ResourceDirectoryDTO dto) {
        service.createResourceDirectory(dto);
        return ResponseEntity.ok("Resource Directory created successfully.");
    }

    @GetMapping("/find {id}")
    public ResponseEntity<ResourceDirectory> getResourceDirectory(@PathVariable UUID id) {
        ResourceDirectory resourceDirectory = service.getResourceDirectoryById(id);
        if (resourceDirectory != null) {
            return ResponseEntity.ok(resourceDirectory);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("findAll")
    public ResponseEntity<List<ResourceDirectory>> getAllResourceDirectories() {
        return ResponseEntity.ok(service.getAllResourceDirectories());
    }

    @PutMapping("/update {id}")
    public ResponseEntity<String> updateResourceDirectory(@PathVariable UUID id, @RequestBody ResourceDirectoryDTO dto) {
        service.updateResourceDirectory(id, dto);
        return ResponseEntity.ok("Resource Directory updated successfully.");
    }

    @DeleteMapping("/delete {id}")
    public ResponseEntity<String> deleteResourceDirectory(@PathVariable UUID id) {
        service.deleteResourceDirectory(id);
        return ResponseEntity.ok("Resource Directory deleted successfully.");
    }
}

