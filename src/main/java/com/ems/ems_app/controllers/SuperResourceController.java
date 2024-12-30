package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.SuperResourceDTO;
import com.ems.ems_app.services.SuperResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("super-resources")
public class SuperResourceController {

    private final SuperResourceService superResourceService;

    public SuperResourceController(SuperResourceService superResourceService) {
        this.superResourceService = superResourceService;
    }

    @PostMapping("create")
    public ResponseEntity<SuperResourceDTO> createSuperResource(@RequestBody SuperResourceDTO dto) {
        return ResponseEntity.ok(superResourceService.createSuperResource(dto));
    }

    @GetMapping("findAll")
    public ResponseEntity<List<SuperResourceDTO>> getAllSuperResources() {
        return ResponseEntity.ok(superResourceService.getAllSuperResources());
    }

    @DeleteMapping("/delete {id}")
    public ResponseEntity<Void> deleteSuperResource(@PathVariable UUID id) {
        superResourceService.deleteSuperResource(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update {id}")
    public ResponseEntity<SuperResourceDTO> updateSuperResource(
            @PathVariable UUID id,
            @RequestBody SuperResourceDTO dto) {
        SuperResourceDTO updatedSuperResource = superResourceService.updateSuperResource(id, dto);
        return ResponseEntity.ok(updatedSuperResource);
    }
}
