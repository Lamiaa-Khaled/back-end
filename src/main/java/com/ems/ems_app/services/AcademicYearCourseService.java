package com.ems.ems_app.services;

import com.ems.ems_app.dto.requestDTO.AcademicYearCourseRequestDTO;
import com.ems.ems_app.dto.requestDTO.AssignCoursesRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearCourseResponseDTO;
import com.ems.ems_app.dto.responseDTO.AssignCoursesResponseDTO;
import com.ems.ems_app.dto.responseDTO.AssignedCourseInfoDTO;
import com.ems.ems_app.entities.AcademicTerm;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.entities.AcademicYearCourse;
import com.ems.ems_app.entities.Course;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.AcademicTermRepository;
import com.ems.ems_app.repos.AcademicYearCourseRepository;
import com.ems.ems_app.repos.AcademicYearRepository;
import com.ems.ems_app.repos.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AcademicYearCourseService {

    private final AcademicYearCourseRepository academicYearCourseRepository;
    private final AcademicYearRepository academicYearRepository;
    private final CourseRepository courseRepository;
    private final AcademicTermRepository academicTermRepository;


    // --- New Service Method for Bulk Assignment ---
    @Transactional // Ensure atomicity: all succeed or all fail
    public AssignCoursesResponseDTO assignCoursesToYearAndTerm(AssignCoursesRequestDTO requestDTO) throws SQLException {
        log.info("Assigning courses {} to year {} and term {}",
                requestDTO.getCourseCodes(), requestDTO.getAcademicYearId(), requestDTO.getTermId());

        // 1. Validate existence of the common AcademicYear and Term
        AcademicYear academicYear = academicYearRepository.findById(requestDTO.getAcademicYearId())
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYear not found with id: " + requestDTO.getAcademicYearId()));

        AcademicTerm term = academicTermRepository.findById(requestDTO.getTermId())
                .orElseThrow(() -> new ResourceNotFoundException("AcademicTerm not found with id: " + requestDTO.getTermId()));

        List<AssignedCourseInfoDTO> assignedCoursesInfo = new ArrayList<>();
        List<AcademicYearCourse> coursesToSave = new ArrayList<>();

        // 2. Iterate through course codes, validate, and prepare entities
        for (String courseCode : requestDTO.getCourseCodes()) {
            // Validate Course existence (assuming Course uses 'code' as ID/lookup key)
            // Adjust findByCode if your CourseRepository uses a different method
            Course course = courseRepository.findByCode(courseCode) // Needs implementation in CourseRepository
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));

            // Check for duplicates (optional but good practice - check if this combination already exists)
            // boolean exists = academicYearCourseRepository.existsByAcademicYearAndTermAndCourse(academicYear, term, course);
            // if (exists) {
            //    log.warn("Course {} already assigned to year {} and term {}. Skipping.", courseCode, academicYear.getId(), term.getId());
            //    // Decide whether to skip, throw error, or include in response differently
            //    continue; // Skip for now
            // }


            AcademicYearCourse newAssignment = AcademicYearCourse.builder()
                    .academicYear(academicYear)
                    .term(term)
                    .course(course) // Assign the validated Course entity
                    .build();
            // Don't set ID or timestamps here, let save handle it

            coursesToSave.add(newAssignment);
        }

        // 3. Save all prepared entities
        // Note: Saving one by one can be inefficient for very large lists.
        // Consider a batch save method in the repository if performance becomes an issue.
        for (AcademicYearCourse assignment : coursesToSave) {
            AcademicYearCourse savedAssignment = academicYearCourseRepository.save(assignment);
            // Add info to the response list *after* successful save
            assignedCoursesInfo.add(AssignedCourseInfoDTO.builder()
                    .courseCode(savedAssignment.getCourse().getCode()) // Get code from saved entity
                    .termId(savedAssignment.getTerm().getId())         // Get termId from saved entity
                    .build());
        }


        log.info("Successfully assigned {} courses to year {} and term {}",
                assignedCoursesInfo.size(), requestDTO.getAcademicYearId(), requestDTO.getTermId());

        // 4. Build and return the response DTO
        return AssignCoursesResponseDTO.builder()
                .message("Courses assigned successfully")
                .assignedCourses(assignedCoursesInfo)
                .build();
    }
    // --- End of New Service Method ---
    @Transactional
    public AcademicYearCourseResponseDTO createCourse(AcademicYearCourseRequestDTO requestDTO) {
        // Optional: Validate existence of academicYearId, courseCode, termId
        AcademicYearCourse academicYearCourse = mapToEntity(requestDTO);
        AcademicYearCourse savedCourse = academicYearCourseRepository.save(academicYearCourse);
        return mapToResponseDTO(savedCourse);
    }

    public AcademicYearCourseResponseDTO getCourseById(UUID id) {
        AcademicYearCourse course = academicYearCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYearCourse not found with id: " + id));
        return mapToResponseDTO(course);
    }

    public List<AcademicYearCourseResponseDTO> getAllCourses() {
        return academicYearCourseRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Add other find methods if needed, e.g., findByAcademicYearId, findByCourseCode etc.
    // public List<AcademicYearCourseResponseDTO> getCoursesByAcademicYear(UUID academicYearId) { ... }

    @Transactional
    public AcademicYearCourseResponseDTO updateCourse(UUID id, AcademicYearCourseRequestDTO requestDTO) {
        AcademicYearCourse existingCourse = academicYearCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYearCourse not found with id: " + id));

        // Optional: Validate new academicYearId, courseCode, termId

        // Update fields
        AcademicYear ay = new AcademicYear();
        ay.setId(requestDTO.getAcademicYearId());
        existingCourse.setAcademicYear(ay);

        Course c = new Course();
        c.setCode(requestDTO.getCourseCode());
        existingCourse.setCourse(c);

        AcademicTerm term = new AcademicTerm();
        term.setId(requestDTO.getTermId());
        existingCourse.setTerm(term);

        AcademicYearCourse updatedCourse = academicYearCourseRepository.update(existingCourse)
                .orElseThrow(() -> new RuntimeException("Failed to update AcademicYearCourse with id: " + id));

        return mapToResponseDTO(updatedCourse);
    }

    @Transactional
    public void deleteCourse(UUID id) {
        boolean deleted = academicYearCourseRepository.deleteById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("AcademicYearCourse not found with id: " + id + " (or deletion failed)");
        }
    }

    // --- Mapping Helpers ---

    private AcademicYearCourse mapToEntity(AcademicYearCourseRequestDTO dto) {
        AcademicYear academicYear = new AcademicYear();
        if (dto.getAcademicYearId() != null) {
            academicYear.setId(dto.getAcademicYearId());
        }

        Course course = new Course();
        if (dto.getCourseCode() != null) {
            course.setCode(dto.getCourseCode()); // Assuming Course uses 'code'
        }

        AcademicTerm term = new AcademicTerm();
        if (dto.getTermId() != null) {
            term.setId(dto.getTermId());
        }

        return AcademicYearCourse.builder()
                .academicYear(academicYear)
                .course(course)
                .term(term)
                .build();
    }

    private AcademicYearCourseResponseDTO mapToResponseDTO(AcademicYearCourse entity) {
        return AcademicYearCourseResponseDTO.builder()
                .id(entity.getId())
                .academicYearId(entity.getAcademicYear() != null ? entity.getAcademicYear().getId() : null)
                .courseCode(entity.getCourse() != null ? entity.getCourse().getCode() : null)
                .termId(entity.getTerm() != null ? entity.getTerm().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
