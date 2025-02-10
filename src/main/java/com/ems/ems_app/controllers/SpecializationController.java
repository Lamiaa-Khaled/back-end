package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.responseDTO.SpecializationResponseDTO;
import com.ems.ems_app.services.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService specializationService;

    @Autowired
    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping
    public ResponseEntity<SpecializationResponseDTO> createSpecialization(@RequestParam String specializationName) {
        System.out.println("Specialization Name received: " + specializationName);

        SpecializationResponseDTO createdSpecialization = specializationService.createSpecialization(specializationName);
        return new ResponseEntity<>(createdSpecialization, HttpStatus.CREATED);
    }

    @GetMapping("/{specializationId}")
    public ResponseEntity<SpecializationResponseDTO> getSpecializationById(@PathVariable UUID specializationId) {
        SpecializationResponseDTO specialization = specializationService.getSpecializationById(specializationId);
        return new ResponseEntity<>(specialization, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SpecializationResponseDTO>> getAllSpecializations() {
        List<SpecializationResponseDTO> specializations = specializationService.getAllSpecializations();
        return new ResponseEntity<>(specializations, HttpStatus.OK);
    }

    @PutMapping("/{specializationId}")
    public ResponseEntity<SpecializationResponseDTO> updateSpecialization(@PathVariable UUID specializationId, @RequestParam String specializationName) {
        SpecializationResponseDTO updatedSpecialization = specializationService.updateSpecialization(specializationId, specializationName);
        return new ResponseEntity<>(updatedSpecialization, HttpStatus.OK);
    }

    @DeleteMapping("/{specializationId}")
    public ResponseEntity<Void> deleteSpecialization(@PathVariable UUID specializationId) {
        specializationService.deleteSpecialization(specializationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}