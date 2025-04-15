package com.ems.ems_app.services;

import com.ems.ems_app.dto.requestDTO.AcademicTermRequestDTO;
import com.ems.ems_app.dto.requestDTO.AcademicYearRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicTermResponseDTO;
import com.ems.ems_app.dto.responseDTO.AssignTermInfoDTO;
import com.ems.ems_app.dto.responseDTO.AssignTermResponseDTO;
import com.ems.ems_app.entities.AcademicTerm;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.exceptions.OperationFailedException;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.AcademicTermRepository;
import com.ems.ems_app.repos.AcademicYearRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AcademicTermService {
    private static final Logger logger = LoggerFactory.getLogger(AcademicYearService.class);

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private AcademicTermRepository academicTermRepository; // Inject Term repo

    @Autowired
    private DataSource dataSource; // Inject DataSource for transaction management

    public AssignTermResponseDTO createAcademicYearWithTerms(AcademicYearRequestDTO dto) {
        // Manual Transaction Management with try-with-resources for Connection
        // This ensures the connection is closed even if exceptions occur
        try (Connection conn = dataSource.getConnection()) {
            try {
                // 1. Start Transaction
                conn.setAutoCommit(false);
                logger.debug("Transaction started for creating academic year {}", dto.getYear());

                // 2. Create and Save AcademicYear Entity
                AcademicYear year = AcademicYear.builder()
                        .year(dto.getYear())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        // Provide a default status if null or empty in the request
                        .status(dto.getStatus() != null && !dto.getStatus().trim().isEmpty() ? dto.getStatus() : "PLANNED")
                        .build();
                // Pass the connection to the repository save method
                AcademicYear savedYear = academicYearRepository.save(year, conn);
                logger.info("Saved AcademicYear with ID: {}", savedYear.getId());


                // 3. Create and Save AcademicTerm Entities
                List<AcademicTerm> savedTerms = new ArrayList<>();
                if (dto.getTerms() != null && !dto.getTerms().isEmpty()) {
                    logger.debug("Processing {} terms for academic year {}", dto.getTerms().size(), savedYear.getYear());
                    for (AcademicTermRequestDTO termDto : dto.getTerms()) {
                        AcademicTerm term = AcademicTerm.builder()
                                .name(termDto.getName())
                                .termOrder(termDto.getTermOrder())
                                .startDate(termDto.getStartDate())
                                .endDate(termDto.getEndDate())
                                .academicYear(savedYear) // Link to the saved year entity
                                .build();

                        // ***** THE FIX IS HERE *****
                        // Pass the SAME connection 'conn' to the term repository save method
                        AcademicTerm savedTerm = academicTermRepository.save(term, conn);
                        savedTerms.add(savedTerm);
                        logger.debug("Saved AcademicTerm '{}' with ID: {}", savedTerm.getName(), savedTerm.getId());
                    }
                } else {
                    logger.debug("No terms provided for academic year {}", savedYear.getYear());
                }

                // 4. Commit Transaction
                conn.commit();
                logger.info("Transaction committed successfully for AcademicYear ID: {}", savedYear.getId());

                // 5. Build Response DTO
                return buildSuccessResponse(savedYear, savedTerms);

            } catch (SQLException e) {
                // 6. Rollback Transaction on Error
                logger.error("SQL error occurred during academic year/term creation. Rolling back transaction.", e);
                try {
                    conn.rollback();
                    logger.info("Transaction rolled back successfully.");
                } catch (SQLException rollbackEx) {
                    // Log rollback failure, but throw original exception
                    logger.error("FATAL: Could not rollback transaction!", rollbackEx);
                }
                // Wrap SQLException in a custom runtime exception
                throw new OperationFailedException("Failed to create academic year and terms due to database error.", e);
            }
            // No finally block needed to reset autoCommit or close connection
            // because try-with-resources handles closing, and autoCommit is session-based.
            // Setting autoCommit back to true might interfere if the connection is pooled.

        } catch (SQLException e) {
            // Handle exception getting the connection itself
            logger.error("Error obtaining database connection.", e);
            throw new OperationFailedException("Database connection error: " + e.getMessage(), e);
        }
    }

    // Helper method to build the specific success response DTO
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


    public AcademicTermResponseDTO getById(UUID id) throws SQLException {
        AcademicTerm term = academicTermRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academic term not found"));
        return toResponseDTO(term);
    }

    public List<AcademicTermResponseDTO> getAll() throws SQLException {
        List<AcademicTermResponseDTO> result = new ArrayList<>();
        for (AcademicTerm t : academicTermRepository.findAll()) {
            result.add(toResponseDTO(t));
        }
        return result;
    }

    public void deleteAcademicTerm(UUID id) throws SQLException {
        getById(id);
        academicTermRepository.deleteById(id);
    }
    private AcademicTermResponseDTO toResponseDTO(AcademicTerm t) {
        return AcademicTermResponseDTO.builder()
                .id(t.getId())
                .name(t.getName())
                .termOrder(t.getTermOrder())
                .startDate(t.getStartDate())
                .endDate(t.getEndDate())
                .status(t.getStatus())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}

