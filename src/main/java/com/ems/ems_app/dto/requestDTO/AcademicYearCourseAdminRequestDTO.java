package com.ems.ems_app.dto.requestDTO;
import lombok.*;
import lombok.Data;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearCourseAdminRequestDTO {
    private UUID academicYearId;
    private String courseCode;
    private UUID adminId;
}
