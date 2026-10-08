package com.hallbooking.service;

import com.hallbooking.entity.Booking;
import com.hallbooking.entity.Hall;
import com.hallbooking.entity.Payment;
import com.hallbooking.repository.BookingRepository;
import com.hallbooking.repository.HallRepository;
import com.hallbooking.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking(7L, 3L, 11L, "2026-10-10", "Morning", "Wedding", 100, null, "approved", null);
        when(bookingRepository.findById(7L)).thenReturn(Optional.of(booking));
        lenient().when(hallRepository.findById(3L)).thenReturn(Optional.of(new Hall(3L, "Grand Palace", null, 200, null, 2500.0, null, null, "active")));
        lenient().when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void completedPaymentPersistsPaidBookingDetails() {
        Payment payment = payment("completed", 11L, 2500.0);

        Payment result = paymentService.processPayment(payment);

        assertEquals("completed", result.getStatus());
        assertNotNull(result.getPaymentDate());
        assertEquals("PAID", booking.getPaymentStatus());
        assertEquals(result.getPaymentDate(), booking.getPaymentDate());
        assertEquals(result.getTransactionId(), booking.getTransactionId());
        assertEquals(2500.0, booking.getPaymentAmount());
        verify(bookingRepository).save(booking);
    }

    @Test
    void pendingPaymentKeepsBookingApprovedAndUnpaid() {
        paymentService.processPayment(payment("pending", 11L, 2500.0));

        assertEquals("approved", booking.getStatus());
        assertEquals("PENDING", booking.getPaymentStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void failedPaymentReturnsBookingToPending() {
        paymentService.processPayment(payment("failed", 11L, 2500.0));

        assertEquals("pending", booking.getStatus());
        assertEquals("FAILED", booking.getPaymentStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void rejectsPaymentWhenUserDoesNotOwnBooking() {
        Payment payment = payment("completed", 12L, 2500.0);

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(payment));

        verify(paymentRepository, never()).save(any(Payment.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void rejectsAmountThatDoesNotMatchHallPrice() {
        Payment payment = payment("completed", 11L, 100.0);

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(payment));

        verify(paymentRepository, never()).save(any(Payment.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    private Payment payment(String status, Long userId, double amount) {
        return new Payment(null, 7L, userId, amount, "Card", "TXN-TEST-1", status, null, null);
    }
}
