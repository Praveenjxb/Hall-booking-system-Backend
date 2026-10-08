package com.hallbooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "bookingId is required")
    @Positive(message = "bookingId must be positive")
    @Column(nullable = false)
    private Long bookingId;

    @NotNull(message = "userId is required")
    @Positive(message = "userId must be positive")
    @Column(nullable = false)
    private Long userId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be positive")
    @Column(nullable = false)
    private Double amount;

    @NotBlank(message = "paymentMethod is required")
    @Size(max = 100, message = "Payment method must be at most 100 characters")
    @Column(nullable = false)
    private String paymentMethod; // "Card", "UPI", "Net Banking", "Cash"

    @Size(max = 100, message = "Transaction ID must be at most 100 characters")
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Pattern(regexp = "(?i)completed|pending|failed|refunded|refund_pending|refund pending", message = "Unsupported payment status")
    @Column(nullable = false)
    private String status = "completed"; // "completed", "pending", "failed", "refunded", "refund_pending"

    @Column(name = "payment_date", nullable = false)
    private String paymentDate;

    @Size(max = 2000, message = "Notes must be at most 2000 characters")
    private String notes;

    @PrePersist
    public void prePersist() {
        if (this.paymentDate == null) {
            this.paymentDate = Instant.now().toString();
        }
        if (this.status == null) {
            this.status = "completed";
        }
        if (this.transactionId == null || this.transactionId.isBlank()) {
            this.transactionId = "TXN-" + System.currentTimeMillis();
        }
    }

    public Payment() {
    }

    public Payment(Long id, Long bookingId, Long userId, Double amount, String paymentMethod, String transactionId, String status, String paymentDate, String notes) {
        this.id = id;
        this.bookingId = bookingId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status = status != null ? status : "completed";
        this.paymentDate = paymentDate != null ? paymentDate : Instant.now().toString();
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
