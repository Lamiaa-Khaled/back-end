package com.ems.ems_app.user_management.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity(name = "user_privilege_assignment")
@Data
public class UserPrivilegeAssignment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private UUID Id;
    @ManyToOne
    @JoinColumn(name = "userId",insertable = false, updatable = false)
    private User user;
    private UUID userId;
    @ManyToOne
    @JoinColumn(name = "privilegeId",insertable = false, updatable = false)
    private Privilege privilege;
    private UUID privilegeId;

}
