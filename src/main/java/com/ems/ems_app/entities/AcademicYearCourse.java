package com.ems.ems_app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "academic_year_courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @ManyToOne
    @JoinColumn(name = "course_code", referencedColumnName = "code", nullable = false) // Reference the Course PK
    private Course course;

    @ManyToOne
    @JoinColumn(name = "term_id", nullable = false)
    private AcademicTerm term;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
