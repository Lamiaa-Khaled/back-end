package com.ems.ems_app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignedCourseInfoDTO {
    private String courseCode;
    private UUID termId;
    // Removed the generated 'id' of the AcademicYearCourse mapping,
    // as it wasn't in the target response structure.
}