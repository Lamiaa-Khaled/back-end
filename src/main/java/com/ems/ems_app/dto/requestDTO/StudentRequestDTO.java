package com.ems.ems_app.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class StudentRequestDTO {

    private UserRequestDTO userRequestDTO;

    @NotNull(message = "Class study ID is required")
    private UUID classStudyId;
}
