package com.ems.ems_app.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue
    private UUID id; // Use UUID for the primary key

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // Field for storing type (e.g., docs, pdf, word, etc.)

    @Column(name = "resource_dir_id")
    private UUID resourceDirId; // Foreign key to ResourceDirectory

    @ManyToOne
    @JoinColumn(name = "resource_dir_id", insertable = false, updatable = false)
    private ResourceDirectory resourceDirectory;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Resource() {
    }

    public Resource(String name, String type, UUID resourceDirId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.type = type;
        this.resourceDirId = resourceDirId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getResourceDirId() {
        return resourceDirId;
    }

    public void setResourceDirId(UUID resourceDirId) {
        this.resourceDirId = resourceDirId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
