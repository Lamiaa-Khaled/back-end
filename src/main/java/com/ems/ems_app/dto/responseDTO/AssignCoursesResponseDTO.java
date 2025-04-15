package com.ems.ems_app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignCoursesResponseDTO {
    private String message;
    private List<AssignedCourseInfoDTO> assignedCourses;
}