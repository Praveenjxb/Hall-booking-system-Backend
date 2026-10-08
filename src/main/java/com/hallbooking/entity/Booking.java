package com.hallbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "hallId is required")
    @Positive(message = "hallId must be positive")
    @Column(nullable = false)
    private Long hallId;

    @NotNull(message = "userId is required")
    @Positive(message = "userId must be positive")
    @Column(nullable = false)
    private Long userId;

    @NotBlank(message = "Date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date must use YYYY-MM-DD format")
    @Column(nullable = false)
    private String date;

    @NotBlank(message = "Slot is required")
    @Pattern(regexp = "(?i)morning|afternoon|evening|full day", message = "Slot must be Morning, Afternoon, Evening, or Full day")
    @Column(nullable = false)
    private String slot; // "Morning", "Afternoon", "Evening", "Full day"

    @Size(max = 100, message = "Event type must be at most 100 characters")
    private String eventType;

    @Positive(message = "Guest count must be positive")
    private Integer guests;

    @Size(max = 2000, message = "Purpose must be at most 2000 characters")
    private String purpose;

    @Pattern(regexp = "(?i)pending|approved|rejected|cancelled", message = "Unsupported booking status")
    @Column(nullable = false)
    private String status = "pending"; // "pending", "approved", "rejected", "cancelled"

    @Pattern(regexp = "(?i)pending|paid|failed|refunded|refund_pending|refund pending", message = "Unsupported payment status")
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "PENDING"; // "PENDING", "PAID", "FAILED", "REFUNDED", "REFUND PENDING"

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "transaction_id")
    @Size(max = 100, message = "Transaction ID must be at most 100 characters")
    private String transactionId;

    @Positive(message = "Payment amount must be positive")
    @Column(name = "payment_amount")
    private Double paymentAmount;

    @Column(name = "cancelled_at")
    private String cancelledAt;

    @Size(max = 2000, message = "Cancellation reason must be at most 2000 characters")
    @Column(name = "cancellation_reason", length = 2000)
    private String cancellationReason;

    @Size(max = 100, message = "Cancellation ID must be at most 100 characters")
    @Column(name = "cancellation_id", length = 100)
    private String cancellationId;

    @Size(max = 50, message = "Refund status must be at most 50 characters")
    @Column(name = "refund_status", length = 50)
    private String refundStatus;

    @Column(name = "refund_amount")
    private Double refundAmount;

    @Size(max = 100, message = "User name must be at most 100 characters")
    @Column(name = "user_name")
    private String userName;

    @Size(max = 254, message = "User email must be at most 254 characters")
    @Column(name = "user_email")
    private String userEmail;

    @Size(max = 255, message = "Hall name must be at most 255 characters")
    @Column(name = "hall_name")
    private String hallName;

    @Size(max = 50, message = "Start time must be at most 50 characters")
    @Column(name = "start_time")
    private String startTime;

    @Size(max = 50, message = "End time must be at most 50 characters")
    @Column(name = "end_time")
    private String endTime;

    @Column(name = "created_at")
    private String createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now().toString();
        }
        if (this.status == null) {
            this.status = "pending";
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = "PENDING";
        }
    }

    public Booking() {
    }

    public Booking(Long id, Long hallId, Long userId, String date, String slot, String eventType, Integer guests, String purpose, String status, String createdAt) {
        this.id = id;
        this.hallId = hallId;
        this.userId = userId;
        this.date = date;
        this.slot = slot;
        this.eventType = eventType;
        this.guests = guests;
        this.purpose = purpose;
        this.status = status != null ? status : "pending";
        this.createdAt = createdAt != null ? createdAt : Instant.now().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHallId() {
        return hallId;
    }

    public void setHallId(Long hallId) {
        this.hallId = hallId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(String cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(String cancellationId) {
        this.cancellationId = cancellationId;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }
}
