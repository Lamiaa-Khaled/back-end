package com.ems.ems_app.entities;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Super_Resource")
public class SuperResource {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id", nullable = false)
    private Resource resource;

    public SuperResource() {}

    public SuperResource(byte[] data, Resource resource) {
        this.data = data;
        this.resource = resource;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}

