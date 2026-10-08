package com.hallbooking.repository;

import com.hallbooking.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    List<Payment> findByBookingId(Long bookingId);

    Optional<Payment> findByTransactionId(String transactionId);

    @Query("SELECT p FROM Payment p WHERE " +
           "(:userId IS NULL OR p.userId = :userId) AND " +
           "(:bookingId IS NULL OR p.bookingId = :bookingId) AND " +
           "(:status IS NULL OR LOWER(p.status) = LOWER(:status)) " +
           "ORDER BY p.id DESC")
    List<Payment> filterPayments(
        @Param("userId") Long userId,
        @Param("bookingId") Long bookingId,
        @Param("status") String status
    );
}
