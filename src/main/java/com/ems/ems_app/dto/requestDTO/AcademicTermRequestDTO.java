package com.ems.ems_app.dto.requestDTO;
//
import lombok.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicTermRequestDTO {
    private UUID academicYearId;
    private String name;
    private int termOrder;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
