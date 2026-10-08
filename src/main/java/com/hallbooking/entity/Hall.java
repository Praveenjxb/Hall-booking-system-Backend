package com.hallbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Hall name is required")
    @Size(max = 150, message = "Hall name must be at most 150 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    @Column(length = 2000)
    private String description;

    @Positive(message = "Capacity must be positive")
    private Integer capacity;

    @Size(max = 255, message = "Location must be at most 255 characters")
    private String location;

    @Positive(message = "Price per slot must be positive")
    private Double pricePerSlot;

    @Size(max = 50, message = "A hall can have at most 50 facilities")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hall_facilities", joinColumns = @JoinColumn(name = "hall_id"))
    @Column(name = "facility")
    private List<@NotBlank @Size(max = 100) String> facilities = new ArrayList<>();

    @Size(max = 20, message = "A hall can have at most 20 images")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hall_images", joinColumns = @JoinColumn(name = "hall_id"))
    @Column(name = "image_url")
    private List<@NotBlank @Size(max = 2048) String> images = new ArrayList<>();

    @Pattern(regexp = "(?i)active|inactive", message = "Status must be active or inactive")
    @Column(nullable = false)
    private String status = "active"; // "active" or "inactive"

    public Hall() {
    }

    public Hall(Long id, String name, String description, Integer capacity, String location, Double pricePerSlot, List<String> facilities, List<String> images, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.location = location;
        this.pricePerSlot = pricePerSlot;
        this.facilities = facilities != null ? facilities : new ArrayList<>();
        this.images = images != null ? images : new ArrayList<>();
        this.status = status != null ? status : "active";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPricePerSlot() {
        return pricePerSlot;
    }

    public void setPricePerSlot(Double pricePerSlot) {
        this.pricePerSlot = pricePerSlot;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities != null ? facilities : new ArrayList<>();
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images != null ? images : new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
