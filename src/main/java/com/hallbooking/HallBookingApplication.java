package com.hallbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HallBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HallBookingApplication.class, args);
        System.out.println("=================================================");
        System.out.println("  Hall Booking Spring Boot Backend is running!   ");
        System.out.println("  API URL:       http://localhost:8080           ");
        System.out.println("=================================================");
    }
}
