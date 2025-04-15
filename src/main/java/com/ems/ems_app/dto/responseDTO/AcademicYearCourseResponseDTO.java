package com.ems.ems_app.dto.responseDTO;
import lombok.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearCourseResponseDTO {
    private UUID id;
    private UUID academicYearId;
    private String courseCode;
    private UUID termId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}