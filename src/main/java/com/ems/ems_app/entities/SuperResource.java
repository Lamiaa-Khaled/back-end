package com.ems.ems_app.entities;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "super_resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperResource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
=======
import lombok.Data;

import java.util.UUID;
@Data
@Entity
@Table(name = "SuperResource")
public class SuperResource {
    @Id
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
    private UUID id;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @OneToOne
<<<<<<< HEAD
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;
=======
    @JoinColumn(name = "resource_id")
    private Resource resource;

    // Getters and Setters
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
}