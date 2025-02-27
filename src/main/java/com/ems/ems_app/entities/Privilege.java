package com.ems.ems_app.entities;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

import lombok.Data;
@Entity(name = "privilege")
@Data
public class Privilege {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private UUID id;
    private String description;
    @ManyToOne
    @JoinColumn(name = "userId",insertable = false, updatable = false)
    private User user;
    private UUID userId;
    @ManyToOne
    @JoinColumn(name = "roleId",insertable = false, updatable = false)
    private Role role;
    private UUID roleId;

    @OneToMany(mappedBy = "privilege")
    private List<UserPrivilegeAssignment> users;


}
