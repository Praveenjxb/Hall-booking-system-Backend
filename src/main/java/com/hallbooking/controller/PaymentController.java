package com.hallbooking.controller;

import com.hallbooking.entity.Payment;
import com.hallbooking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> getPayments(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) String status) {
        return paymentService.getPayments(userId, bookingId, status);
    }

    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @GetMapping("/booking/{bookingId}")
    public List<Payment> getPaymentsByBookingId(@PathVariable Long bookingId) {
        return paymentService.getPaymentsByBookingId(bookingId);
    }

    @PostMapping
    public ResponseEntity<Payment> processPayment(@Valid @RequestBody Payment payment) {
        Payment created = paymentService.processPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        Payment refunded = paymentService.refundPayment(id);
        return ResponseEntity.ok(refunded);
    }
}
