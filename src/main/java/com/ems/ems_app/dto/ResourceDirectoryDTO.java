package com.ems.ems_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class ResourceDirectoryDTO {
    private String name;
    private String creator;

    // Getters and Setters
}

