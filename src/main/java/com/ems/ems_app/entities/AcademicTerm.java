package com.ems.ems_app.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// AcademicTerm Entity
@Entity
@Table(name = "academic_terms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"academic_year_id", "term_order"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private UUID id;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    private String name;
    @Column(name = "term_order", nullable = false)

    private int termOrder;
    @Column(name = "start_date")

    private LocalDate startDate;
    @Column(name = "end_date")

    private LocalDate endDate;
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)

    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")

    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<AcademicYearCourse> academicYearCourses = new HashSet<>();
}
