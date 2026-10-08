package com.hallbooking.service;

import com.hallbooking.entity.Booking;
import com.hallbooking.exception.ResourceNotFoundException;
import com.hallbooking.repository.BookingRepository;
import com.hallbooking.validation.EntityValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EntityValidator entityValidator;
    private final com.hallbooking.repository.NotificationRepository notificationRepository;
    private final com.hallbooking.repository.HallRepository hallRepository;
    private final com.hallbooking.repository.UserRepository userRepository;
    private final com.hallbooking.repository.PaymentRepository paymentRepository;

    @org.springframework.beans.factory.annotation.Autowired
    public BookingService(BookingRepository bookingRepository,
                          EntityValidator entityValidator,
                          com.hallbooking.repository.NotificationRepository notificationRepository,
                          com.hallbooking.repository.HallRepository hallRepository,
                          com.hallbooking.repository.UserRepository userRepository,
                          com.hallbooking.repository.PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.entityValidator = entityValidator;
        this.notificationRepository = notificationRepository;
        this.hallRepository = hallRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    public BookingService(BookingRepository bookingRepository,
                          EntityValidator entityValidator,
                          com.hallbooking.repository.NotificationRepository notificationRepository,
                          com.hallbooking.repository.HallRepository hallRepository,
                          com.hallbooking.repository.UserRepository userRepository) {
        this(bookingRepository, entityValidator, notificationRepository, hallRepository, userRepository, null);
    }

    public BookingService(BookingRepository bookingRepository, EntityValidator entityValidator) {
        this(bookingRepository, entityValidator, null, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookings(Long hallId, Long userId, String status, String date) {
        List<Booking> list = bookingRepository.filterBookings(hallId, userId, status, date);
        for (Booking b : list) {
            populateBookingMetadataIfMissing(b);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(Long id) {
        Booking b = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        populateBookingMetadataIfMissing(b);
        return b;
    }

    public Booking createBooking(Booking booking) {
        validateDate(booking.getDate());

        // Initial booking status defaults to pending
        if (booking.getStatus() == null || booking.getStatus().isBlank()) {
            booking.setStatus("pending");
        }

        // Prevent booking conflicts if an approved booking exists for this slot
        if ("approved".equalsIgnoreCase(booking.getStatus())) {
            List<Booking> conflicts = bookingRepository.findByHallIdAndDateAndSlotAndStatus(
                    booking.getHallId(), booking.getDate(), booking.getSlot(), "approved");
            if (!conflicts.isEmpty()) {
                throw new IllegalArgumentException("This slot is already booked and approved for the selected hall.");
            }
        }

        // Populate User Name and Email if missing
        if ((booking.getUserName() == null || booking.getUserName().isBlank() ||
             booking.getUserEmail() == null || booking.getUserEmail().isBlank()) &&
            userRepository != null && booking.getUserId() != null) {
            userRepository.findById(booking.getUserId()).ifPresent(u -> {
                if (booking.getUserName() == null || booking.getUserName().isBlank()) {
                    booking.setUserName(u.getName());
                }
                if (booking.getUserEmail() == null || booking.getUserEmail().isBlank()) {
                    booking.setUserEmail(u.getEmail());
                }
            });
        }

        // Populate Hall Name if missing
        if ((booking.getHallName() == null || booking.getHallName().isBlank()) &&
            hallRepository != null && booking.getHallId() != null) {
            hallRepository.findById(booking.getHallId()).ifPresent(h -> {
                booking.setHallName(h.getName());
            });
        }

        // Populate Start and End Time based on slot if missing
        enrichSlotTimes(booking);

        entityValidator.validate(booking);
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking patch) {
        Booking existing = getBookingById(id);
        String oldStatus = existing.getStatus();
        if (patch.getStatus() != null && !patch.getStatus().isBlank()) {
            existing.setStatus(patch.getStatus());
        }
        if (patch.getDate() != null && !patch.getDate().isBlank()) {
            existing.setDate(patch.getDate());
        }
        if (patch.getSlot() != null && !patch.getSlot().isBlank()) {
            existing.setSlot(patch.getSlot());
        }
        if (patch.getEventType() != null) {
            existing.setEventType(patch.getEventType());
        }
        if (patch.getGuests() != null) {
            existing.setGuests(patch.getGuests());
        }
        if (patch.getPurpose() != null) {
            existing.setPurpose(patch.getPurpose());
        }
        if (patch.getStartTime() != null && !patch.getStartTime().isBlank()) {
            existing.setStartTime(patch.getStartTime());
        }
        if (patch.getEndTime() != null && !patch.getEndTime().isBlank()) {
            existing.setEndTime(patch.getEndTime());
        }
        if (patch.getUserName() != null && !patch.getUserName().isBlank()) {
            existing.setUserName(patch.getUserName());
        }
        if (patch.getUserEmail() != null && !patch.getUserEmail().isBlank()) {
            existing.setUserEmail(patch.getUserEmail());
        }
        if (patch.getHallName() != null && !patch.getHallName().isBlank()) {
            existing.setHallName(patch.getHallName());
        }
        if (patch.getPaymentStatus() != null && !patch.getPaymentStatus().isBlank()) {
            existing.setPaymentStatus(patch.getPaymentStatus());
        }
        if (patch.getCancelledAt() != null && !patch.getCancelledAt().isBlank()) {
            existing.setCancelledAt(patch.getCancelledAt());
        }
        if (patch.getCancellationReason() != null) {
            existing.setCancellationReason(patch.getCancellationReason());
        }
        if (patch.getCancellationId() != null && !patch.getCancellationId().isBlank()) {
            existing.setCancellationId(patch.getCancellationId());
        }
        if (patch.getRefundStatus() != null && !patch.getRefundStatus().isBlank()) {
            existing.setRefundStatus(patch.getRefundStatus());
        }
        if (patch.getRefundAmount() != null) {
            existing.setRefundAmount(patch.getRefundAmount());
        }
        validateDate(existing.getDate());
        enrichSlotTimes(existing);
        entityValidator.validate(existing);
        Booking saved = bookingRepository.save(existing);

        if (notificationRepository != null && patch.getStatus() != null && !patch.getStatus().isBlank()) {
            String newStatus = patch.getStatus().trim().toLowerCase();
            String prevStatus = oldStatus != null ? oldStatus.trim().toLowerCase() : "";
            if (!newStatus.equals(prevStatus) && ("approved".equals(newStatus) || "rejected".equals(newStatus))) {
                try {
                    String hallName = saved.getHallName();
                    if ((hallName == null || hallName.isBlank()) && hallRepository != null && saved.getHallId() != null) {
                        hallName = hallRepository.findById(saved.getHallId())
                                .map(com.hallbooking.entity.Hall::getName)
                                .orElse("Hall #" + saved.getHallId());
                    }
                    String displayStatus = Character.toUpperCase(newStatus.charAt(0)) + newStatus.substring(1);
                    com.hallbooking.entity.Notification notification = new com.hallbooking.entity.Notification();
                    notification.setUserId(saved.getUserId());
                    notification.setBookingId(saved.getId());
                    notification.setTitle("Booking " + displayStatus);
                    notification.setMessage(String.format("Your booking for %s on %s (%s) has been %s.",
                            hallName, saved.getDate(), saved.getSlot() != null ? saved.getSlot() : "standard slot", newStatus));
                    notification.setStatus(newStatus);
                    notification.setHallName(hallName);
                    notification.setBookingDate(saved.getDate());
                    notification.setBookingTime(saved.getSlot());
                    notification.setRead(false);
                    notificationRepository.save(notification);
                } catch (Exception ignored) {
                }
            }
        }

        return saved;
    }

    public Booking cancelBooking(Long id, String reason) {
        Booking existing = getBookingById(id);
        existing.setStatus("cancelled");
        existing.setCancelledAt(java.time.Instant.now().toString());
        if (reason != null && !reason.isBlank()) {
            existing.setCancellationReason(reason.trim());
        } else if (existing.getCancellationReason() == null) {
            existing.setCancellationReason("Cancelled by user");
        }
        if (existing.getCancellationId() == null || existing.getCancellationId().isBlank()) {
            existing.setCancellationId("CAN-HB-" + existing.getId() + "-" + System.currentTimeMillis());
        }

        boolean wasPaid = "PAID".equalsIgnoreCase(existing.getPaymentStatus()) ||
                "COMPLETED".equalsIgnoreCase(existing.getPaymentStatus()) ||
                (existing.getPaymentAmount() != null && existing.getPaymentAmount() > 0);

        if (!wasPaid && paymentRepository != null) {
            List<com.hallbooking.entity.Payment> pList = paymentRepository.findByBookingId(existing.getId());
            wasPaid = pList != null && pList.stream().anyMatch(p -> "completed".equalsIgnoreCase(p.getStatus()) || "paid".equalsIgnoreCase(p.getStatus()));
        }

        if (wasPaid) {
            existing.setPaymentStatus("REFUND PENDING");
            existing.setRefundStatus("Refund Pending");
            if (existing.getRefundAmount() == null || existing.getRefundAmount() <= 0) {
                if (existing.getPaymentAmount() != null && existing.getPaymentAmount() > 0) {
                    existing.setRefundAmount(existing.getPaymentAmount());
                } else if (paymentRepository != null) {
                    List<com.hallbooking.entity.Payment> payments = paymentRepository.findByBookingId(existing.getId());
                    double sum = payments.stream().mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0).sum();
                    if (sum > 0) {
                        existing.setRefundAmount(sum);
                        existing.setPaymentAmount(sum);
                    }
                }
                if ((existing.getRefundAmount() == null || existing.getRefundAmount() <= 0) && hallRepository != null && existing.getHallId() != null) {
                    hallRepository.findById(existing.getHallId()).ifPresent(h -> {
                        double amt = h.getPricePerSlot() != null ? h.getPricePerSlot() : 0;
                        if ("Full day".equalsIgnoreCase(existing.getSlot())) amt *= 2;
                        existing.setRefundAmount(amt);
                        existing.setPaymentAmount(amt);
                    });
                }
            }

            // Update linked payment records
            if (paymentRepository != null) {
                try {
                    List<com.hallbooking.entity.Payment> payments = paymentRepository.findByBookingId(existing.getId());
                    for (com.hallbooking.entity.Payment p : payments) {
                        p.setStatus("refund_pending");
                        String note = "Refund pending (" + existing.getCancellationId() + ")";
                        p.setNotes(p.getNotes() != null ? p.getNotes() + "; " + note : note);
                        paymentRepository.save(p);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        entityValidator.validate(existing);
        Booking saved = bookingRepository.save(existing);

        // Send notifications
        if (notificationRepository != null) {
            try {
                String hallName = saved.getHallName();
                if ((hallName == null || hallName.isBlank()) && hallRepository != null && saved.getHallId() != null) {
                    hallName = hallRepository.findById(saved.getHallId())
                            .map(com.hallbooking.entity.Hall::getName)
                            .orElse("Hall #" + saved.getHallId());
                }
                String timeStr = (saved.getStartTime() != null && saved.getEndTime() != null)
                        ? saved.getStartTime() + " – " + saved.getEndTime()
                        : (saved.getSlot() != null ? saved.getSlot() : "Session");

                if (wasPaid) {
                    String amountFormatted = String.format("₹%,.0f", saved.getPaymentAmount() != null ? saved.getPaymentAmount() : 0.0);

                    // User notification
                    com.hallbooking.entity.Notification userNotif = new com.hallbooking.entity.Notification();
                    userNotif.setUserId(saved.getUserId());
                    userNotif.setBookingId(saved.getId());
                    userNotif.setTitle("Booking Cancelled");
                    userNotif.setMessage(String.format("Your booking for %s on %s, %s has been cancelled successfully.\n\nAmount Paid: %s\nRefund Status: Refund Pending",
                            hallName, saved.getDate(), timeStr, amountFormatted));
                    userNotif.setStatus("cancelled");
                    userNotif.setHallName(hallName);
                    userNotif.setBookingDate(saved.getDate());
                    userNotif.setBookingTime(timeStr);
                    userNotif.setRead(false);
                    notificationRepository.save(userNotif);

                    // Admin notifications
                    if (userRepository != null) {
                        List<com.hallbooking.entity.User> admins = userRepository.findByRole("admin");
                        for (com.hallbooking.entity.User admin : admins) {
                            com.hallbooking.entity.Notification adminNotif = new com.hallbooking.entity.Notification();
                            adminNotif.setUserId(admin.getId());
                            adminNotif.setBookingId(saved.getId());
                            adminNotif.setTitle("Booking Cancelled by User");
                            adminNotif.setMessage(String.format("User %s cancelled paid booking #%d for %s on %s (%s). Amount Paid: %s. Refund Status: Refund Pending.",
                                    saved.getUserName() != null ? saved.getUserName() : "User #" + saved.getUserId(),
                                    saved.getId(), hallName, saved.getDate(), timeStr, amountFormatted));
                            adminNotif.setStatus("cancelled");
                            adminNotif.setHallName(hallName);
                            adminNotif.setBookingDate(saved.getDate());
                            adminNotif.setBookingTime(timeStr);
                            adminNotif.setRead(false);
                            notificationRepository.save(adminNotif);
                        }
                    }
                } else {
                    // Unpaid cancellation notification
                    com.hallbooking.entity.Notification userNotif = new com.hallbooking.entity.Notification();
                    userNotif.setUserId(saved.getUserId());
                    userNotif.setBookingId(saved.getId());
                    userNotif.setTitle("Booking Cancelled");
                    userNotif.setMessage(String.format("Your booking request for %s on %s (%s) has been cancelled.",
                            hallName, saved.getDate(), timeStr));
                    userNotif.setStatus("cancelled");
                    userNotif.setHallName(hallName);
                    userNotif.setBookingDate(saved.getDate());
                    userNotif.setBookingTime(timeStr);
                    userNotif.setRead(false);
                    notificationRepository.save(userNotif);
                }
            } catch (Exception ignored) {
            }
        }

        return saved;
    }

    public Booking processRefund(Long id) {
        Booking existing = getBookingById(id);
        existing.setPaymentStatus("REFUNDED");
        existing.setRefundStatus("Refunded");
        if (existing.getRefundAmount() == null && existing.getPaymentAmount() != null) {
            existing.setRefundAmount(existing.getPaymentAmount());
        }

        // Update linked payment records
        if (paymentRepository != null) {
            try {
                List<com.hallbooking.entity.Payment> payments = paymentRepository.findByBookingId(existing.getId());
                for (com.hallbooking.entity.Payment p : payments) {
                    p.setStatus("refunded");
                    String note = "Refund completed";
                    p.setNotes(p.getNotes() != null ? p.getNotes() + "; " + note : note);
                    paymentRepository.save(p);
                }
            } catch (Exception ignored) {
            }
        }

        entityValidator.validate(existing);
        Booking saved = bookingRepository.save(existing);

        // Notify user about completed refund
        if (notificationRepository != null) {
            try {
                String hallName = saved.getHallName();
                if ((hallName == null || hallName.isBlank()) && hallRepository != null && saved.getHallId() != null) {
                    hallName = hallRepository.findById(saved.getHallId())
                            .map(com.hallbooking.entity.Hall::getName)
                            .orElse("Hall #" + saved.getHallId());
                }
                double refundAmt = saved.getRefundAmount() != null ? saved.getRefundAmount() : (saved.getPaymentAmount() != null ? saved.getPaymentAmount() : 0.0);
                String amountFormatted = String.format("₹%,.0f", refundAmt);

                com.hallbooking.entity.Notification userNotif = new com.hallbooking.entity.Notification();
                userNotif.setUserId(saved.getUserId());
                userNotif.setBookingId(saved.getId());
                userNotif.setTitle("Refund Processed");
                userNotif.setMessage(String.format("Your refund of %s for booking #%d (%s on %s) has been processed successfully.",
                        amountFormatted, saved.getId(), hallName, saved.getDate()));
                userNotif.setStatus("refunded");
                userNotif.setHallName(hallName);
                userNotif.setBookingDate(saved.getDate());
                userNotif.setBookingTime(saved.getSlot());
                userNotif.setRead(false);
                notificationRepository.save(userNotif);
            } catch (Exception ignored) {
            }
        }

        return saved;
    }

    private void enrichSlotTimes(Booking booking) {
        if (booking.getSlot() == null) return;
        String slot = booking.getSlot().trim();
        if (booking.getStartTime() == null || booking.getStartTime().isBlank()) {
            if ("Morning".equalsIgnoreCase(slot)) booking.setStartTime("09:00 AM");
            else if ("Afternoon".equalsIgnoreCase(slot)) booking.setStartTime("01:00 PM");
            else if ("Evening".equalsIgnoreCase(slot)) booking.setStartTime("05:00 PM");
            else if ("Full day".equalsIgnoreCase(slot)) booking.setStartTime("09:00 AM");
        }
        if (booking.getEndTime() == null || booking.getEndTime().isBlank()) {
            if ("Morning".equalsIgnoreCase(slot)) booking.setEndTime("01:00 PM");
            else if ("Afternoon".equalsIgnoreCase(slot)) booking.setEndTime("05:00 PM");
            else if ("Evening".equalsIgnoreCase(slot)) booking.setEndTime("10:00 PM");
            else if ("Full day".equalsIgnoreCase(slot)) booking.setEndTime("10:00 PM");
        }
    }

    private void populateBookingMetadataIfMissing(Booking b) {
        if ((b.getUserName() == null || b.getUserEmail() == null) && userRepository != null && b.getUserId() != null) {
            userRepository.findById(b.getUserId()).ifPresent(u -> {
                if (b.getUserName() == null || b.getUserName().isBlank()) b.setUserName(u.getName());
                if (b.getUserEmail() == null || b.getUserEmail().isBlank()) b.setUserEmail(u.getEmail());
            });
        }
        if ((b.getHallName() == null || b.getHallName().isBlank()) && hallRepository != null && b.getHallId() != null) {
            hallRepository.findById(b.getHallId()).ifPresent(h -> b.setHallName(h.getName()));
        }
        enrichSlotTimes(b);
    }

    private void validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException | NullPointerException ex) {
            throw new IllegalArgumentException("Date must be a valid calendar date in YYYY-MM-DD format.");
        }
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
}
