package com.ems.ems_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseDTO {
    private String code;
    private String name;
    private UUID avatarId;
    private String groupName;
    private String resourceDirectoryName;

    // Constructors, Getters and Setters
}

