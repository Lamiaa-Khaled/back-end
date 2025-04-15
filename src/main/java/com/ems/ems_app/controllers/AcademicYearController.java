package com.ems.ems_app.controllers;

import com.ems.ems_app.dto.requestDTO.AcademicYearRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearResponseDTO;
import com.ems.ems_app.dto.responseDTO.AssignTermInfoDTO;
import com.ems.ems_app.dto.responseDTO.AssignTermResponseDTO;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.services.AcademicYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/academic-years")
public class AcademicYearController {

    @Autowired
    private AcademicYearService service;

    @PostMapping
   public ResponseEntity<AssignTermResponseDTO> createAcademicYearWithTerms(@RequestBody AcademicYearRequestDTO request) {
       // No throws SQLException needed here if service handles/wraps it
       AssignTermResponseDTO response = service.createAcademicYearWithTerms(request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
   }


    @GetMapping("/{id}")
    public ResponseEntity<AcademicYearResponseDTO> getById(@PathVariable UUID id) throws SQLException {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AcademicYearResponseDTO>> getAll() throws SQLException {
        return ResponseEntity.ok(service.getAll());
    }
   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAcademicYear(@PathVariable UUID id) throws SQLException {
        service.deleteAcademicYear(id);
        return ResponseEntity.ok("Academic Year deleted successfully.");
    }
}
