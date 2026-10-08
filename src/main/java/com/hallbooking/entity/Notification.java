package com.hallbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "userId is required")
    @Positive(message = "userId must be positive")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Positive(message = "bookingId must be positive")
    @Column(name = "booking_id")
    private Long bookingId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 5000, message = "Message must be at most 5000 characters")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @NotBlank(message = "Status is required")
    @Size(max = 50, message = "Status must be at most 50 characters")
    @Column(nullable = false)
    private String status; // "approved", "rejected", "pending", "info"

    @Size(max = 255, message = "Hall name must be at most 255 characters")
    @Column(name = "hall_name")
    private String hallName;

    @Size(max = 10, message = "Booking date must be at most 10 characters")
    @Column(name = "booking_date")
    private String bookingDate;

    @Size(max = 50, message = "Booking time must be at most 50 characters")
    @Column(name = "booking_time")
    private String bookingTime;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "created_at")
    private String createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now().toString();
        }
    }

    public Notification() {
    }

    public Notification(Long id, Long userId, Long bookingId, String title, String message, String status, String hallName, String bookingDate, String bookingTime, boolean isRead, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.bookingId = bookingId;
        this.title = title;
        this.message = message;
        this.status = status;
        this.hallName = hallName;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("isRead")
    public boolean isRead() {
        return isRead;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("isRead")
    public void setRead(boolean read) {
        isRead = read;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("read")
    public boolean getRead() {
        return isRead;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
