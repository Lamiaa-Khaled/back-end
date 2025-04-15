package com.ems.ems_app.services;

import com.ems.ems_app.dto.requestDTO.AcademicTermRequestDTO;
import com.ems.ems_app.dto.requestDTO.AcademicYearRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearResponseDTO;
import com.ems.ems_app.dto.responseDTO.AssignTermInfoDTO;
import com.ems.ems_app.dto.responseDTO.AssignTermResponseDTO;
import com.ems.ems_app.entities.AcademicTerm;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.exceptions.OperationFailedException;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.AcademicTermRepository;
import com.ems.ems_app.repos.AcademicYearRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AcademicYearService {


    @Autowired
    private AcademicYearRepository repository;


    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private AcademicTermRepository academicTermRepository; // Inject Term repo

    @Autowired
    private DataSource dataSource; // Inject DataSource for transaction management

    // *** MODIFIED: create method to handle year and terms transactionally ***
    public AssignTermResponseDTO createAcademicYearWithTerms(AcademicYearRequestDTO dto) {
        // Manual Transaction Management with try-with-resources for Connection
        try (Connection conn = dataSource.getConnection()) {
            try {
                // 1. Start Transaction
                conn.setAutoCommit(false);

                // 2. Create and Save AcademicYear Entity
                AcademicYear year = AcademicYear.builder()
                        .year(dto.getYear())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .status(dto.getStatus() != null ? dto.getStatus() : "Active") // Default if not provided
                        .build();
                AcademicYear savedYear = academicYearRepository.save(year, conn); // Pass connection

                // 3. Create and Save AcademicTerm Entities
                List<AcademicTerm> savedTerms = new ArrayList<>();
                if (dto.getTerms() != null && !dto.getTerms().isEmpty()) {
                    for (AcademicTermRequestDTO termDto : dto.getTerms()) {
                        AcademicTerm term = AcademicTerm.builder()
                                .name(termDto.getName())
                                .termOrder(termDto.getTermOrder())
                                .startDate(termDto.getStartDate())
                                .endDate(termDto.getEndDate())
                                .academicYear(savedYear) // Link to the saved year
                                .build();
                        savedTerms.add(academicTermRepository.save(term, conn)); // Pass connection
                    }
                }

                // 4. Commit Transaction
                conn.commit();

                // 5. Build Response DTO
                return buildSuccessResponse(savedYear, savedTerms);

            } catch (SQLException e) {
                // 6. Rollback Transaction on Error
                conn.rollback(); // Rollback if any SQL error occurred
                // Log the exception (using a logger is recommended)
                System.err.println("Error creating academic year/terms: " + e.getMessage());
                throw new OperationFailedException("Failed to create academic year and terms. Error: " + e.getMessage(), e);
            } finally {
                // Ensure autoCommit is reset (though try-with-resources should close conn)
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            }
        } catch (SQLException e) {
            // Handle exception getting the connection itself
            System.err.println("Error obtaining database connection: " + e.getMessage());
            throw new OperationFailedException("Database connection error: " + e.getMessage(), e);
        }
    }

    // Helper method to build the specific response DTO
    private AssignTermResponseDTO buildSuccessResponse(AcademicYear year, List<AcademicTerm> terms) {
        List<AssignTermInfoDTO> termResponses = terms.stream()
                .map(term -> AssignTermInfoDTO.builder()
                        .termId(term.getId())
                        .name(term.getName())
                        .build())
                .collect(Collectors.toList());

        return AssignTermResponseDTO.builder()
                .message("Academic year created successfully")
                .academicYearId(year.getId())
                .assignedTerms(termResponses)
                .build();
    }


    public AcademicYearResponseDTO getById(UUID id) throws SQLException {
        AcademicYear year = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic Year not found"));
        return toResponseDTO(year);
    }

    public List<AcademicYearResponseDTO> getAll() throws SQLException {
        List<AcademicYear> list = repository.findAll();
        List<AcademicYearResponseDTO> response = new ArrayList<>();
        for (AcademicYear y : list) response.add(toResponseDTO(y));
        return response;
    }

    private AcademicYearResponseDTO toResponseDTO(AcademicYear entity) {
        return AcademicYearResponseDTO.builder()
                .id(entity.getId())
                .year(entity.getYear())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void deleteAcademicYear(UUID id) throws SQLException {
        getById(id);
        academicYearRepository.deleteById(id);
    }

}
