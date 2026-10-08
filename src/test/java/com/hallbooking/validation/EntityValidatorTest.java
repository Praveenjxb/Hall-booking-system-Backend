package com.hallbooking.validation;

import com.hallbooking.entity.Booking;
import com.hallbooking.repository.BookingRepository;
import com.hallbooking.service.BookingService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EntityValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static EntityValidator entityValidator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        entityValidator = new EntityValidator(validatorFactory.getValidator());
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void rejectsInvalidBookingFields() {
        Booking booking = new Booking(
                null, 0L, -1L, "2026-10-08", "overnight", null, -3, null, "unknown", null);

        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> entityValidator.validate(booking));

        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("hallId")));
        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("slot")));
        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("guests")));
        assertTrue(exception.getConstraintViolations().stream()
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("status")));
    }

    @Test
    void rejectsImpossibleCalendarDateInBookingUpdate() {
        BookingRepository repository = mock(BookingRepository.class);
        Booking existing = new Booking(
                1L, 2L, 3L, "2026-10-08", "Morning", null, 10, null, "pending", null);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        BookingService service = new BookingService(repository, entityValidator);
        Booking patch = new Booking();
        patch.setDate("2026-02-30");

        assertThrows(IllegalArgumentException.class, () -> service.updateBooking(1L, patch));

    }
}
