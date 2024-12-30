package com.ems.ems_app.dto;

import java.util.UUID;

public class ResourceDTO {

    private String name;
    private String type; // Field for type
    private UUID resourceDirId; // Foreign key to ResourceDirectory

    // Constructors
    public ResourceDTO() {
    }

    public ResourceDTO(String name, String type, UUID resourceDirId) {
        this.name = name;
        this.type = type;
        this.resourceDirId = resourceDirId;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getResourceDirId() {
        return resourceDirId;
    }

    public void setResourceDirId(UUID resourceDirId) {
        this.resourceDirId = resourceDirId;
    }
}
