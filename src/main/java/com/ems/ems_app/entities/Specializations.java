package com.ems.ems_app.entities;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "specializations")
@Data
public class Specializations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Long specializationId;

    @Column(name = "specialization_name", nullable = false, unique = true, length = 100)
    private String specializationName;

    public Long getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Long specializationId) {
        this.specializationId = specializationId;
    }

    public String getSpecializationName() {
        return specializationName;
    }

    public void setSpecializationName(String specializationName) {
        this.specializationName = specializationName;
    }
}