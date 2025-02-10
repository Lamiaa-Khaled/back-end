package com.ems.ems_app.dto.responseDTO;

import com.ems.ems_app.entities.Student;
import lombok.Data;

import java.util.UUID;


@Data
public class StudentResponseDTO {
    private UserResponseDTO userResponseDTO;
    private UUID classStudyId;

    public static StudentResponseDTO convertToStudentResponseDTO(Student student) {
        StudentResponseDTO responseDTO = new StudentResponseDTO();
        responseDTO.setUserResponseDTO(UserResponseDTO.convertToUserResponseDTO(student.getUser()));
        responseDTO.setClassStudyId(student.getClassStudy().getClassId());
        return responseDTO;
    }
}
