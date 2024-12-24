package com.ems.ems_app.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
@Table(name = "SuperResource")
public class SuperResource {
    @Id
    private UUID id;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    // Getters and Setters
}