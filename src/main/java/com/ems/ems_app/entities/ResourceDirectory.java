package com.ems.ems_app.entities;
<<<<<<< HEAD

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import jakarta.persistence.*;
import lombok.Data;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

<<<<<<< HEAD
@Entity
@Table(name = "resource_directory")
@Data
@NoArgsConstructor
@AllArgsConstructor
=======
@Data
@Entity
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
public class ResourceDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

<<<<<<< HEAD
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String creator;

    @Column(name = "base_dir_id")
    private UUID baseDirId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
=======
    private String name;
    private String creator;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Getters and Setters
}

>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
