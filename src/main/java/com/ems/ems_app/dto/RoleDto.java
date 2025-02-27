package com.ems.ems_app.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleDto {
    private UUID roleId;
    private String description;
    private String details;
}
