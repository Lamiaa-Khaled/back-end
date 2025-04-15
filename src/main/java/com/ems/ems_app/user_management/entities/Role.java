package com.ems.ems_app.user_management.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Auditable;
@Entity(name = "role")
@Data
public class Role  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id")
    private UUID roleId;
    private String description;
    private String details;

    @OneToMany(mappedBy = "role")
    private List <Privilege> privileges;

}
