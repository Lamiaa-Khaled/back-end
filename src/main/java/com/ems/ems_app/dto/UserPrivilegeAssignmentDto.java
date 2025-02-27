package com.ems.ems_app.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserPrivilegeAssignmentDto {
    private UUID id;
    private UUID userId;
    private UUID privilegeId;
}
