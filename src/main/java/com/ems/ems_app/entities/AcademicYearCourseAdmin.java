package com.ems.ems_app.entities;

import com.ems.ems_app.user_management.entities.Admin;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.GenericGenerator; // For UUID generation strategy

import java.time.LocalDateTime;
import java.util.UUID;

// AcademicYearCourseAdmin Entity
@Entity
@Table(name = "academic_year_course_admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYearCourseAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

//    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @ManyToOne
    @JoinColumn(name = "course_code", referencedColumnName = "code", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}

