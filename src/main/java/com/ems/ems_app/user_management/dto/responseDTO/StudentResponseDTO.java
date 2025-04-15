package com.ems.ems_app.user_management.dto.responseDTO;

import com.ems.ems_app.dto.responseDTO.GroupResponseDTO;
import com.ems.ems_app.user_management.entities.Student;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


@Data
public class StudentResponseDTO {
    private UserResponseDTO userResponseDTO;
    private GroupResponseDTO groupResponseDTO;

    public static StudentResponseDTO convertToStudentResponseDTO(Student student) {
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setUserResponseDTO(UserResponseDTO.convertToUserResponseDTO(student.getUser()));
        responseDTO.setGroupResponseDTO(GroupResponseDTO.fromEntity(student.getGroup()));
        return responseDTO;

    }
}
