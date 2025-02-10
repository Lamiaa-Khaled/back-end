package com.ems.ems_app.dto.responseDTO;

import com.ems.ems_app.entities.Admin;
import lombok.Data;

@Data
public class AdminResponseDTO {
    private UserResponseDTO userResponseDTO;
    private SpecializationResponseDTO specializationResponseDTO;

    public static AdminResponseDTO convertToAdminResponseDTO(Admin admin) {
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        responseDTO.setUserResponseDTO(UserResponseDTO.convertToUserResponseDTO(admin.getUser()));
        responseDTO.setSpecializationResponseDTO(SpecializationResponseDTO.convertToSpecializationResponseDTO(admin.getSpecialization()));
        return responseDTO;
    }
}
