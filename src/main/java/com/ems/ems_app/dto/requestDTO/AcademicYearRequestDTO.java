package com.ems.ems_app.dto.requestDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearRequestDTO {
    private int year;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private List<AcademicTermRequestDTO> terms;

}