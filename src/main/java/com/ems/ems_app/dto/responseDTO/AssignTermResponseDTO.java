package com.ems.ems_app.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignTermResponseDTO {
    private String message;
    private UUID academicYearId;
    private List<AssignTermInfoDTO> assignedTerms;
}
