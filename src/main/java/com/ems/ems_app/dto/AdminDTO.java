package com.ems.ems_app.dto;


import lombok.Data;


public class AdminDTO {
    private Long adminId;
    private Long userId; // or UserDTO, depending on your needs
    private Long specializationId; // or SpecializationDTO

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Long specializationId) {
        this.specializationId = specializationId;
    }
}