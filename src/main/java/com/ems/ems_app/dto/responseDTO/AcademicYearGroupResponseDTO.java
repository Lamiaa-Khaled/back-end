package com.ems.ems_app.dto.responseDTO;

import lombok.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearGroupResponseDTO {
    private UUID id;
    private UUID academicYearId;
    private UUID groupId;
    private int yearNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
