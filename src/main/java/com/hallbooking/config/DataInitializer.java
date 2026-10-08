package com.hallbooking.config;

import com.hallbooking.entity.Booking;
import com.hallbooking.entity.Hall;
import com.hallbooking.entity.User;
import com.hallbooking.repository.BookingRepository;
import com.hallbooking.repository.HallRepository;
import com.hallbooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HallRepository hallRepository;
    private final BookingRepository bookingRepository;

    public DataInitializer(UserRepository userRepository, HallRepository hallRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.hallRepository = hallRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedHalls();
        seedBookings();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;

        User admin = new User(
                "Admin",
                "admin@hallbook.com",
                "Admin@123",
                "9876500001",
                "admin"
        );

        User praveen = new User(
                "Praveen",
                "praveen12@gmail.com",
                "User@123",
                "9876500002",
                "user"
        );

        User arjun = new User(
                "Arjun Mehta",
                "arjun@example.com",
                "User@123",
                "9876500003",
                "user"
        );

        User kavya = new User(
                "Kavya Nair",
                "kavya@example.com",
                "User@123",
                "9876500004",
                "user"
        );

        userRepository.saveAll(Arrays.asList(admin, praveen, arjun, kavya));
    }

    private void seedHalls() {
        if (hallRepository.count() > 0) return;

        Hall h1 = new Hall(
                null,
                "Grand Crystal Ballroom",
                "A palatial ballroom with crystal chandeliers, a stage and a dance floor. Ideal for weddings and receptions.",
                500,
                "Adyar, Chennai",
                45000.0,
                Arrays.asList("AC", "Stage", "Parking", "Catering", "Sound System", "WiFi"),
                Arrays.asList(
                        "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1478147427282-58a87a120781?auto=format&fit=crop&w=1200&q=80"
                ),
                "active"
        );

        Hall h2 = new Hall(
                null,
                "Summit Conference Centre",
                "Tiered seating, dual projectors and simultaneous interpretation booths for large conferences.",
                300,
                "Tidel Park Road, Chennai",
                25000.0,
                Arrays.asList("AC", "Projector", "WiFi", "Sound System", "Parking"),
                Arrays.asList(
                        "https://images.unsplash.com/photo-1511578314322-379afb476865?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=1200&q=80"
                ),
                "active"
        );

        Hall h3 = new Hall(
                null,
                "Lotus Workshop Hall",
                "Flexible, bright hall with movable furniture for workshops, trainings and small seminars.",
                80,
                "RS Puram, Coimbatore",
                12000.0,
                Arrays.asList("AC", "Projector", "WiFi", "Whiteboards"),
                Arrays.asList(
                        "https://images.unsplash.com/photo-1431540015161-0bf868a2d407?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1200&q=80"
                ),
                "active"
        );

        Hall h4 = new Hall(
                null,
                "Jasmine Terrace Garden",
                "Open-air terrace with string lights and a garden backdrop for birthdays and cocktail evenings.",
                120,
                "Alwarpet, Chennai",
                18000.0,
                Arrays.asList("Open Air", "Catering", "Sound System", "Parking"),
                Arrays.asList(
                        "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80"
                ),
                "active"
        );

        Hall h5 = new Hall(
                null,
                "Executive Boardroom",
                "A quiet, premium boardroom for leadership meetings, interviews and small seminars.",
                40,
                "Anna Nagar, Chennai",
                8000.0,
                Arrays.asList("AC", "Projector", "WiFi", "Video Conferencing"),
                Arrays.asList(
                        "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1431540015161-0bf868a2d407?auto=format&fit=crop&w=1200&q=80"
                ),
                "active"
        );

        Hall h6 = new Hall(
                null,
                "Royal Palace Banquet",
                "Traditional banquet hall with an in-house kitchen, bridal suites and generous parking.",
                300,
                "Race Course, Coimbatore",
                30000.0,
                Arrays.asList("AC", "Stage", "Parking", "Catering", "Bridal Suite"),
                Arrays.asList(
                        "https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80",
                        "https://images.unsplash.com/photo-1478147427282-58a87a120781?auto=format&fit=crop&w=1200&q=80"
                ),
                "active"
        );

        hallRepository.saveAll(Arrays.asList(h1, h2, h3, h4, h5, h6));
    }

    private void seedBookings() {
        if (bookingRepository.count() > 0) return;

        List<Booking> bookings = Arrays.asList(
                new Booking(null, 1L, 2L, "2026-08-15", "Evening", "Wedding", 350, "Wedding reception", "approved", "2026-07-01T09:30:00.000Z"),
                new Booking(null, 2L, 3L, "2026-09-10", "Full day", "Conference", 180, "Annual tech conference", "approved", "2026-07-20T11:00:00.000Z"),
                new Booking(null, 1L, 4L, "2026-10-18", "Evening", "Wedding", 400, "Wedding ceremony and dinner", "approved", "2026-08-25T14:10:00.000Z"),
                new Booking(null, 3L, 2L, "2026-10-05", "Morning", "Workshop", 60, "Design thinking workshop", "approved", "2026-09-02T08:45:00.000Z"),
                new Booking(null, 4L, 3L, "2026-10-12", "Afternoon", "Birthday", 90, "50th birthday lunch", "pending", "2026-09-20T16:20:00.000Z"),
                new Booking(null, 1L, 3L, "2026-10-18", "Morning", "Birthday", 200, "Family birthday brunch", "pending", "2026-09-22T10:05:00.000Z"),
                new Booking(null, 5L, 4L, "2026-11-02", "Full day", "Seminar", 40, "Leadership offsite", "pending", "2026-09-25T13:40:00.000Z"),
                new Booking(null, 2L, 2L, "2026-11-20", "Evening", "Corporate", 250, "Company annual day", "rejected", "2026-09-05T12:00:00.000Z"),
                new Booking(null, 6L, 4L, "2026-12-05", "Evening", "Wedding", 220, "Engagement function", "approved", "2026-09-15T15:30:00.000Z"),
                new Booking(null, 3L, 3L, "2026-10-25", "Afternoon", "Workshop", 50, "Coding bootcamp session", "cancelled", "2026-09-18T09:00:00.000Z")
        );

        bookingRepository.saveAll(bookings);
    }
}
