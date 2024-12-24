package com.ems.ems_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class GroupDTO {
    private String name;
    private String description;

    // Getters and Setters
}