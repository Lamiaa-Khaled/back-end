package com.ems.ems_app.dto.requestDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {
    @NotBlank(message = "Course code is required")
    @Size(min = 5, max = 10, message = "Course code must be between 5 and 10 characters")
    private String code;

    @NotBlank(message = "Course name is required")
    @Size(max = 100, message = "Course name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Group ID is required")
    private UUID groupId;

    private byte[] avatar;
    private String avatarType;

    @NotNull(message = "Base Directory ID is required")
    private UUID baseDirectoryId;

    private boolean active = true;

}