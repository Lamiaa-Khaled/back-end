package com.ems.ems_app.dto.responseDTO;

import com.ems.ems_app.entities.Specialization;
import lombok.Data;

import java.util.UUID;

@Data
public class SpecializationResponseDTO {
    private UUID specializationId;
    private String specializationName;

    public static SpecializationResponseDTO convertToSpecializationResponseDTO(Specialization specialization) {
        SpecializationResponseDTO specializationResponseDTO = new SpecializationResponseDTO();
        specializationResponseDTO.setSpecializationId(specialization.getSpecializationId());
        specializationResponseDTO.setSpecializationName(specialization.getSpecializationName()); // Use specialization.getSpecializationName()
        return specializationResponseDTO;
    }

}
