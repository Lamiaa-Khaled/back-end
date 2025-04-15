package com.ems.ems_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class ResourceDTO {
    private String name;
    private String type;
    private String resourceDirectoryId;

    // Getters and Setters
}

