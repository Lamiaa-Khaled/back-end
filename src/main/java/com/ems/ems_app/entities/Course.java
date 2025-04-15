<<<<<<< HEAD
// Corrected Course Entity
package com.ems.ems_app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
=======
package com.ems.ems_app.entities;
import jakarta.persistence.*;
import lombok.Data;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa

import java.time.LocalDateTime;
import java.util.UUID;

<<<<<<< HEAD
@Entity
@Table(name = "course")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @Column(name = "code", nullable = false)
=======
@Data
@Entity
@Table(name = "Course")
public class Course {
    @Id
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    private String code;

    @Column(nullable = false)
    private String name;

<<<<<<< HEAD
    @Column(name = "avatar_id")
    private UUID avatarId;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "dir_doc_id", nullable = false)
    private ResourceDirectory baseDirectory;
}
=======
    private UUID avatarId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "dir_doc_id")
    private ResourceDirectory resourceDirectory;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
