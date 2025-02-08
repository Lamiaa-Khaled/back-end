package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.SpecializationsDTO;
import com.ems.ems_app.entities.Specializations;
import com.ems.ems_app.services.SpecializationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("specializations")
public class SpecializationController {

    @Autowired
    private SpecializationsService specializationsService;

    @GetMapping("/{id}")
    public ResponseEntity<SpecializationsDTO> getSpecializationById(@PathVariable Long id) {
        SpecializationsDTO specializationsDTO = specializationsService.getSpecializationById(id);
        return ResponseEntity.ok(specializationsDTO);
    }

    @GetMapping("findAll")
    public List<SpecializationsDTO> getAllSpecializations() {
        List<SpecializationsDTO> specializations = specializationsService.getAllSpecializations();
        return specializations;
    }

    @PostMapping
    public ResponseEntity<SpecializationsDTO> createSpecialization(@RequestBody Specializations specializations) {
        SpecializationsDTO createdSpecialization = specializationsService.createSpecialization(specializations);
        return new ResponseEntity<>(createdSpecialization, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecializationsDTO> updateSpecialization(@PathVariable Long id, @RequestBody Specializations specializationDetails) {
        SpecializationsDTO updatedSpecialization = specializationsService.updateSpecialization(id, specializationDetails);
        return ResponseEntity.ok(updatedSpecialization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable Long id) {
        specializationsService.deleteSpecialization(id);
        return ResponseEntity.noContent().build();
    }
}