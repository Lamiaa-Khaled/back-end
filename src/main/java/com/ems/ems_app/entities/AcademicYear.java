package com.ems.ems_app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "academic_years")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear {
    @Id
    private UUID id;

    @Column(unique = true)
    private int year;
    @Column(name = "start_date")

    private LocalDate startDate;
    @Column(name = "end_date")

    private LocalDate endDate;
    private String status="Active";

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

@OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, orphanRemoval = true)
private List<AcademicTerm> terms = new ArrayList<>();
}