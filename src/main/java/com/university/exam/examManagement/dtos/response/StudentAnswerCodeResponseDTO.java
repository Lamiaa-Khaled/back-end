package com.university.exam.examManagement.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
public class StudentAnswerCodeResponseDTO {
    private UUID studentExamAttemptId;
    private List<CodeAnswer> answers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CodeAnswer {
        private UUID id;
        private UUID examQuestionId;
        private String submittedCode;
        private UUID languageId;
    }
}