package com.ems.ems_app.entities;
<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
=======

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
@Data
@Entity
@Table(name = "Groups")
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

<<<<<<< HEAD
    @Column(nullable = false)
    private String name;

    private String description;
}
=======
    private String name;
    private String description;

    // Getters and Setters
}
>>>>>>> 09c6eba79e900850b77163b30bf5dacde38a56fa
