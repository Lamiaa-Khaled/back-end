package com.ems.ems_app.services;

import com.ems.ems_app.dto.requestDTO.AcademicYearCourseAdminRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearCourseAdminResponseDTO;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.entities.AcademicYearCourseAdmin;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.AcademicYearCourseAdminRepository;
import com.ems.ems_app.user_management.entities.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicYearCourseAdminService {

    private final AcademicYearCourseAdminRepository academicYearCourseAdminRepository;

    @Transactional
    public AcademicYearCourseAdminResponseDTO createAdminAssignment(AcademicYearCourseAdminRequestDTO requestDTO) {
        AcademicYearCourseAdmin adminAssignment = mapToEntity(requestDTO);
        AcademicYearCourseAdmin savedAssignment = academicYearCourseAdminRepository.save(adminAssignment);
        return mapToResponseDTO(savedAssignment);
    }

    public AcademicYearCourseAdminResponseDTO getAdminAssignmentById(UUID id) {
        AcademicYearCourseAdmin assignment = academicYearCourseAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYearCourseAdmin assignment not found with id: " + id));
        return mapToResponseDTO(assignment);
    }

    public List<AcademicYearCourseAdminResponseDTO> getAllAdminAssignments() {
        return academicYearCourseAdminRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Add other find methods if needed (e.g., findByAdminId, findByCourseCodeAndAcademicYear)

    @Transactional
    public AcademicYearCourseAdminResponseDTO updateAdminAssignment(UUID id, AcademicYearCourseAdminRequestDTO requestDTO) {
        AcademicYearCourseAdmin existingAssignment = academicYearCourseAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYearCourseAdmin assignment not found with id: " + id));


        // Update fields
        AcademicYear ay = new AcademicYear();
        ay.setId(requestDTO.getAcademicYearId());
        existingAssignment.setAcademicYear(ay);

        Course c = new Course();
        c.setCode(requestDTO.getCourseCode());
        existingAssignment.setCourse(c);

        Admin admin = new Admin();
        admin.setAdminId(requestDTO.getAdminId());
        existingAssignment.setAdmin(admin);

        AcademicYearCourseAdmin updatedAssignment = academicYearCourseAdminRepository.update(existingAssignment)
                .orElseThrow(() -> new RuntimeException("Failed to update AcademicYearCourseAdmin assignment with id: " + id));

        return mapToResponseDTO(updatedAssignment);
    }

    @Transactional
    public void deleteAdminAssignment(UUID id) {
        boolean deleted = academicYearCourseAdminRepository.deleteById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("AcademicYearCourseAdmin assignment not found with id: " + id + " (or deletion failed)");
        }
    }

    // --- Mapping Helpers ---

    private AcademicYearCourseAdmin mapToEntity(AcademicYearCourseAdminRequestDTO dto) {
        AcademicYear academicYear = new AcademicYear();
        if (dto.getAcademicYearId() != null) {
            academicYear.setId(dto.getAcademicYearId());
        }

        Course course = new Course();
        if (dto.getCourseCode() != null) {
            course.setCode(dto.getCourseCode());
        }

        Admin admin = new Admin();
        if (dto.getAdminId() != null) {
            admin.setAdminId(dto.getAdminId()); // Assuming Admin uses UUID 'id'
        }
        return AcademicYearCourseAdmin.builder()
                .academicYear(academicYear)
                .course(course)
                .admin(admin)
                .build();
    }

    private AcademicYearCourseAdminResponseDTO mapToResponseDTO(AcademicYearCourseAdmin entity) {
        return AcademicYearCourseAdminResponseDTO.builder()
                .id(entity.getId())
                .academicYearId(entity.getAcademicYear() != null ? entity.getAcademicYear().getId() : null)
                .courseCode(entity.getCourse() != null ? entity.getCourse().getCode() : null)
                .adminId(entity.getAdmin() != null ? entity.getAdmin().getAdminId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
