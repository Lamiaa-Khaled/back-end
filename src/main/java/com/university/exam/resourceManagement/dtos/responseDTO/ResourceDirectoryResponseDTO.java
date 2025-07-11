package com.university.exam.resourceManagement.dtos.responseDTO;

import com.university.exam.resourceManagement.entities.ResourceDirectory;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResourceDirectoryResponseDTO {
    private UUID id;
    private String name;
    private String creator;
    private UUID baseDirId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResourceDirectoryResponseDTO fromEntity(ResourceDirectory directory) {
        ResourceDirectoryResponseDTO dto = new ResourceDirectoryResponseDTO();
        dto.setId(directory.getId());
        dto.setName(directory.getName());
        dto.setCreator(directory.getCreator());
        dto.setBaseDirId(directory.getBaseDirId());
        dto.setCreatedAt(directory.getCreatedAt());
        dto.setUpdatedAt(directory.getUpdatedAt());
        return dto;
    }
}