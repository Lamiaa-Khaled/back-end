package com.ems.ems_app.entities;
<<<<<<< HEAD

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
=======
import jakarta.persistence.*;
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
<<<<<<< HEAD
@Table(name = "resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
=======
@Table(name = "Resource")
public class Resource {
    @Id
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @ManyToOne
<<<<<<< HEAD
    @JoinColumn(name = "resource_dir_id", nullable = false)
    private ResourceDirectory resourceDirectory;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

=======
    @JoinColumn(name = "resource_dir_id")
    private ResourceDirectory resourceDirectory;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
