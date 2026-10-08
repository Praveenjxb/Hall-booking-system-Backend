package com.hallbooking.repository;

import com.hallbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE " +
           "(:hallId IS NULL OR b.hallId = :hallId) AND " +
           "(:userId IS NULL OR b.userId = :userId) AND " +
           "(:status IS NULL OR LOWER(b.status) = LOWER(:status)) AND " +
           "(:date IS NULL OR b.date = :date) " +
           "ORDER BY b.id DESC")
    List<Booking> filterBookings(
        @Param("hallId") Long hallId,
        @Param("userId") Long userId,
        @Param("status") String status,
        @Param("date") String date
    );

    List<Booking> findByHallIdAndDateAndSlotAndStatus(Long hallId, String date, String slot, String status);
}
