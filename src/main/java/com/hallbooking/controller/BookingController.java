package com.hallbooking.controller;

import com.hallbooking.entity.Booking;
import com.hallbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getBookings(
            @RequestParam(required = false) Long hallId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date) {

        return bookingService.getBookings(hallId, userId, status, date);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        Booking created = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public Booking patchBooking(@PathVariable Long id, @RequestBody Booking patch) {
        return bookingService.updateBooking(id, patch);
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable Long id, @RequestBody Booking patch) {
        return bookingService.updateBooking(id, patch);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id, @RequestBody(required = false) java.util.Map<String, String> body) {
        String reason = body != null ? body.get("reason") : null;
        Booking cancelled = bookingService.cancelBooking(id, reason);
        return ResponseEntity.ok(cancelled);
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<Booking> refundBooking(@PathVariable Long id) {
        Booking refunded = bookingService.processRefund(id);
        return ResponseEntity.ok(refunded);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
