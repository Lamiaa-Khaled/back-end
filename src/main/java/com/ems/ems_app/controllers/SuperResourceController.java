package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.SuperResourceDTO;
import com.ems.ems_app.entities.SuperResource;
import com.ems.ems_app.services.SuperResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/super-resources")
public class SuperResourceController {
    private final SuperResourceService superResourceService;

    public SuperResourceController(SuperResourceService superResourceService) {
        this.superResourceService = superResourceService;
    }

    @GetMapping
    public List<SuperResource> getAllSuperResources() {
        return superResourceService.getAllSuperResources();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuperResource> getSuperResourceById(@PathVariable UUID id) {
        return superResourceService.getSuperResourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SuperResource createSuperResource(@RequestBody SuperResourceDTO superResourceDTO) {
        SuperResource superResource = new SuperResource();
        superResource.setData(superResourceDTO.getData().getBytes());
        //superResource.setResource(superResourceService.getSuperResourceById(UUID.fromString(superResourceDTO.getResourceId())).orElseThrow());
        return superResourceService.createOrUpdateSuperResource(superResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuperResource(@PathVariable UUID id) {
        superResourceService.deleteSuperResource(id);
        return ResponseEntity.noContent().build();
    }
}
