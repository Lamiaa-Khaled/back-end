package com.ems.ems_app.dto.requestDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignCoursesRequestDTO {

    @NotNull(message = "Academic Year ID cannot be null")
    private UUID academicYearId;

    @NotNull(message = "Term ID cannot be null")
    private UUID termId;

    @NotEmpty(message = "Course codes list cannot be empty")
    private List<String> courseCodes;
}