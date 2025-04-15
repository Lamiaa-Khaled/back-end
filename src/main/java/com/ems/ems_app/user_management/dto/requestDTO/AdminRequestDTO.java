package com.ems.ems_app.user_management.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AdminRequestDTO {
    private UserRequestDTO userRequestDTO;

    @NotNull(message = "Specialization ID is required")
    private UUID specializationId;
}
