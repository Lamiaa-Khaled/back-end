package com.ems.ems_app.controllers;
import com.ems.ems_app.dto.ResourceDTO;
import com.ems.ems_app.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping("create")
    public ResponseEntity<String> createResource(@RequestBody ResourceDTO resourceDTO) {
        resourceService.createResource(resourceDTO);
        return ResponseEntity.ok("Resource created successfully");
    }

    @GetMapping("findAll")
    public List<ResourceDTO> getAllResources() {
        return resourceService.getAllResources();
    }

    @GetMapping("/find {id}")
    public ResponseEntity<ResourceDTO> getResourceById(@PathVariable UUID id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @PutMapping("/update {id}")
    public ResponseEntity<String> updateResource(@PathVariable UUID id, @RequestBody ResourceDTO resourceDTO) {
        resourceService.updateResource(id, resourceDTO);
        return ResponseEntity.ok("Resource updated successfully");
    }

    @DeleteMapping("/delete {id}")
    public ResponseEntity<String> deleteResource(@PathVariable UUID id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok("Resource deleted successfully");
    }
}
