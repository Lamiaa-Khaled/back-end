package com.ems.ems_app.dto;


import lombok.Data;

public class StudentDTO {
    private Long studentId;
    private Long userId; // or UserDTO, depending on your needs
    private Long classStudyId; // or ClassStudyDTO

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClassStudyId() {
        return classStudyId;
    }

    public void setClassStudyId(Long classStudyId) {
        this.classStudyId = classStudyId;
    }
}
