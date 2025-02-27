package com.ems.ems_app.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PrivilegeDto {
    private UUID id;
    private String description;
    private UUID userId;
    private UUID roleId;
}

