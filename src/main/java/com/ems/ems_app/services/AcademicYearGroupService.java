package com.ems.ems_app.services;

import com.ems.ems_app.dto.requestDTO.AcademicYearGroupRequestDTO;
import com.ems.ems_app.dto.responseDTO.AcademicYearGroupResponseDTO;
import com.ems.ems_app.entities.AcademicYear;
import com.ems.ems_app.entities.AcademicYearGroup;
import com.ems.ems_app.entities.Group;
import com.ems.ems_app.exceptions.ResourceNotFoundException;
import com.ems.ems_app.repos.AcademicYearGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AcademicYearGroupService {

    private final AcademicYearGroupRepository academicYearGroupRepository;

    @Transactional // Optional: Ensures atomicity
    public AcademicYearGroupResponseDTO createGroup(AcademicYearGroupRequestDTO requestDTO) {
        AcademicYearGroup academicYearGroup = mapToEntity(requestDTO);
        AcademicYearGroup savedGroup = academicYearGroupRepository.save(academicYearGroup);
        return mapToResponseDTO(savedGroup);
    }

    public AcademicYearGroupResponseDTO getGroupById(UUID id) {
        AcademicYearGroup group = academicYearGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYearGroup not found with id: " + id));
        return mapToResponseDTO(group);
    }

    public List<AcademicYearGroupResponseDTO> getAllGroups() {
        return academicYearGroupRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AcademicYearGroupResponseDTO> getGroupsByAcademicYear(UUID academicYearId) {
        return academicYearGroupRepository.findByAcademicYearId(academicYearId).stream() // Assumes repository has this method
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional // Optional: Ensures atomicity
    public AcademicYearGroupResponseDTO updateGroup(UUID id, AcademicYearGroupRequestDTO requestDTO) {
        AcademicYearGroup existingGroup = academicYearGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcademicYearGroup not found with id: " + id));


        // Update fields from DTO
        AcademicYear academicYear = new AcademicYear();
        academicYear.setId(requestDTO.getAcademicYearId());
        existingGroup.setAcademicYear(academicYear);

        Group group = new Group();
        group.setId(requestDTO.getGroupId());
        existingGroup.setGroup(group);

        existingGroup.setYearNumber(requestDTO.getYearNumber());

        // Use the repository's update method
        AcademicYearGroup updatedGroup = academicYearGroupRepository.update(existingGroup)
                // The update method should ideally return the updated object or confirm success
                .orElseThrow(() -> new RuntimeException("Failed to update AcademicYearGroup with id: " + id)); // Or a more specific update failure exception

        return mapToResponseDTO(updatedGroup);
    }

    @Transactional // Optional: Ensures atomicity
    public void deleteGroup(UUID id) {
        // Check if it exists before trying to delete (optional, deleteById might handle it)
        // if (!academicYearGroupRepository.existsById(id)) { // Assumes existsById or similar check
        //     throw new ResourceNotFoundException("AcademicYearGroup not found with id: " + id);
        // }
        boolean deleted = academicYearGroupRepository.deleteById(id);
        if (!deleted) {
            // This might occur if the record was deleted between a check and the delete call,
            // or if findById wasn't called first.
            throw new ResourceNotFoundException("AcademicYearGroup not found with id: " + id + " (or deletion failed)");
        }
    }

    // --- Mapping Helpers ---

    private AcademicYearGroup mapToEntity(AcademicYearGroupRequestDTO dto) {
        AcademicYear academicYear = new AcademicYear();
        if (dto.getAcademicYearId() != null) {
            academicYear.setId(dto.getAcademicYearId());
        }

        Group group = new Group();
        if (dto.getGroupId() != null) {
            group.setId(dto.getGroupId());
        }

        return AcademicYearGroup.builder()
                .academicYear(academicYear)
                .group(group)
                .yearNumber(dto.getYearNumber())
                // id, createdAt, updatedAt are set by the repository
                .build();
    }

    private AcademicYearGroupResponseDTO mapToResponseDTO(AcademicYearGroup entity) {
        return AcademicYearGroupResponseDTO.builder()
                .id(entity.getId())
                .academicYearId(entity.getAcademicYear() != null ? entity.getAcademicYear().getId() : null)
                .groupId(entity.getGroup() != null ? entity.getGroup().getId() : null)
                .yearNumber(entity.getYearNumber())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}