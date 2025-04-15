package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.responseDTO.AcademicTermResponseDTO;
import com.ems.ems_app.services.AcademicTermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/academic-terms")
public class AcademicTermController {
    @Autowired
    private AcademicTermService service;

    @GetMapping("/{id}")
    public ResponseEntity<AcademicTermResponseDTO> getById(@PathVariable UUID id) throws SQLException {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AcademicTermResponseDTO>> getAll() throws SQLException {
        return ResponseEntity.ok(service.getAll());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAcademicTerm(@PathVariable UUID id) throws SQLException {
        service.deleteAcademicTerm(id);
        return ResponseEntity.ok("Academic term deleted successfully.");
    }
}

