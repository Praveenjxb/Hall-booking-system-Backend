package com.hallbooking.service;

import com.hallbooking.entity.Booking;
import com.hallbooking.entity.Hall;
import com.hallbooking.entity.Payment;
import com.hallbooking.exception.ResourceNotFoundException;
import com.hallbooking.repository.BookingRepository;
import com.hallbooking.repository.HallRepository;
import com.hallbooking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final HallRepository hallRepository;

    public PaymentService(PaymentRepository paymentRepository, BookingRepository bookingRepository, HallRepository hallRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.hallRepository = hallRepository;
    }

    @Transactional(readOnly = true)
    public List<Payment> getPayments(Long userId, Long bookingId, String status) {
        return paymentRepository.filterPayments(userId, bookingId, status);
    }

    @Transactional(readOnly = true)
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Payment> getPaymentsByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public Payment processPayment(Payment payment) {
        Booking booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + payment.getBookingId()));

        if (!"approved".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalArgumentException("Only approved bookings can be paid.");
        }
        if ("PAID".equalsIgnoreCase(booking.getPaymentStatus())) {
            throw new IllegalArgumentException("This booking has already been paid.");
        }
        if (!booking.getUserId().equals(payment.getUserId())) {
            throw new IllegalArgumentException("Payment user does not match the booking owner.");
        }

        Hall hall = hallRepository.findById(booking.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with id: " + booking.getHallId()));
        double expectedAmount = hall.getPricePerSlot() == null ? 0 : hall.getPricePerSlot();
        if ("Full day".equalsIgnoreCase(booking.getSlot())) {
            expectedAmount *= 2;
        }
        if (payment.getAmount() == null || Double.compare(payment.getAmount(), expectedAmount) != 0) {
            throw new IllegalArgumentException("Payment amount does not match the booking total.");
        }

        if (payment.getTransactionId() == null || payment.getTransactionId().isBlank()) {
            payment.setTransactionId("TXN-" + System.currentTimeMillis());
        }

        payment.setPaymentDate(Instant.now().toString());

        if (payment.getStatus() == null || payment.getStatus().isBlank()) {
            payment.setStatus("completed");
        }

        String status = payment.getStatus().toLowerCase();
        if (!List.of("completed", "pending", "failed").contains(status)) {
            throw new IllegalArgumentException("Unsupported payment status: " + payment.getStatus());
        }
        payment.setStatus(status);
        booking.setPaymentDate(payment.getPaymentDate());
        booking.setTransactionId(payment.getTransactionId());
        booking.setPaymentAmount(payment.getAmount());
        if ("completed".equals(status)) {
            booking.setPaymentStatus("PAID");
        } else if ("failed".equals(status)) {
            booking.setPaymentStatus("FAILED");
            booking.setStatus("pending");
        } else if ("pending".equals(status)) {
            booking.setPaymentStatus("PENDING");
        }

        Payment saved = paymentRepository.save(payment);
        bookingRepository.save(booking);
        return saved;
    }

    public Payment refundPayment(Long id) {
        Payment payment = getPaymentById(id);
        payment.setStatus("refunded");

        // Update booking's payment status
        bookingRepository.findById(payment.getBookingId()).ifPresent(b -> {
            b.setPaymentStatus("REFUNDED");
            bookingRepository.save(b);
        });

        return paymentRepository.save(payment);
    }
}
