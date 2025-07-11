package com.university.exam.examManagement.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentChoiceViewDTO {
    private UUID id;
    private String choiceText;
} 