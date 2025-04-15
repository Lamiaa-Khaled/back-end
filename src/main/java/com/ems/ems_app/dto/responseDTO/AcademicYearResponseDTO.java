package com.ems.ems_app.dto.responseDTO;

import lombok.Data;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
// Optional: Include related data summaries if needed
// import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearResponseDTO {
    private UUID id;
    private int year;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
