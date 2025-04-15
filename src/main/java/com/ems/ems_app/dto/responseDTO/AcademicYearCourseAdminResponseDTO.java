package com.ems.ems_app.dto.responseDTO;
import lombok.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearCourseAdminResponseDTO {
    private UUID id;
    private UUID academicYearId;
    private String courseCode;
    private UUID adminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

