package com.ems.ems_app.dto;


public class SuperResourceDTO {

    private byte[] data;
    private String resourceName;

    public SuperResourceDTO() {}

    public SuperResourceDTO(byte[] data, String resourceName) {
        this.data = data;
        this.resourceName = resourceName;
    }

    // Getters and Setters
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
