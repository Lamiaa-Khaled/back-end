package com.ems.ems_app.dto;

import lombok.Data;

import java.math.BigDecimal;

public class ClassStudyDTO {
    private Long classId;
    private Integer year;
    private BigDecimal totalGrade;

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(BigDecimal totalGrade) {
        this.totalGrade = totalGrade;
    }
}