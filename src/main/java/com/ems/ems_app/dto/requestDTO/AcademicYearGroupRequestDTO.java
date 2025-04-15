package com.ems.ems_app.dto.requestDTO;


import lombok.*;
import lombok.Data;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearGroupRequestDTO {
    private UUID academicYearId;
    private UUID groupId;
    private int yearNumber;
}

