-- ========================================================
-- Hall Booking System — MySQL Database Schema & Seed Data
-- ========================================================

CREATE DATABASE IF NOT EXISTS `hall_booking_db`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `hall_booking_db`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(20) DEFAULT NULL,
    `role` VARCHAR(50) NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Halls Table
CREATE TABLE IF NOT EXISTS `halls` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `capacity` INT DEFAULT NULL,
    `location` VARCHAR(255) DEFAULT NULL,
    `price_per_slot` DOUBLE DEFAULT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Hall Facilities Table (Collection)
CREATE TABLE IF NOT EXISTS `hall_facilities` (
    `hall_id` BIGINT NOT NULL,
    `facility` VARCHAR(255) NOT NULL,
    CONSTRAINT `fk_hall_facilities_hall` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Hall Images Table (Collection)
CREATE TABLE IF NOT EXISTS `hall_images` (
    `hall_id` BIGINT NOT NULL,
    `image_url` VARCHAR(1000) NOT NULL,
    CONSTRAINT `fk_hall_images_hall` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Bookings Table
CREATE TABLE IF NOT EXISTS `bookings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `hall_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `booking_date` VARCHAR(50) NOT NULL,
    `slot` VARCHAR(50) NOT NULL,
    `event_type` VARCHAR(100) DEFAULT NULL,
    `guests` INT DEFAULT NULL,
    `purpose` TEXT DEFAULT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'pending',
    `payment_status` VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    `payment_date` VARCHAR(100) DEFAULT NULL,
    `transaction_id` VARCHAR(255) DEFAULT NULL,
    `payment_amount` DOUBLE DEFAULT NULL,
    `created_at` VARCHAR(100) DEFAULT NULL,
    CONSTRAINT `fk_bookings_hall` FOREIGN KEY (`hall_id`) REFERENCES `halls` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_bookings_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Payments Table
CREATE TABLE IF NOT EXISTS `payments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `booking_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `amount` DOUBLE NOT NULL,
    `payment_method` VARCHAR(255) NOT NULL,
    `transaction_id` VARCHAR(255) NOT NULL,
    `status` VARCHAR(50) NOT NULL DEFAULT 'completed',
    `payment_date` VARCHAR(100) NOT NULL,
    `notes` VARCHAR(255) DEFAULT NULL,
    CONSTRAINT `fk_payments_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_payments_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================================
-- Seed Initial Demo Data (Admin, Users, Halls, Bookings)
-- ========================================================

-- Seed Users
INSERT INTO `users` (`id`, `name`, `email`, `password`, `phone`, `role`) VALUES
(1, 'Admin', 'admin@hallbook.com', 'Admin@123', '9876500001', 'admin'),
(2, 'Praveen', 'praveen12@gmail.com', 'User@123', '9876500002', 'user'),
(3, 'Arjun Mehta', 'arjun@example.com', 'User@123', '9876500003', 'user'),
(4, 'Kavya Nair', 'kavya@example.com', 'User@123', '9876500004', 'user')
ON DUPLICATE KEY UPDATE `email`=`email`;

-- Seed Halls
INSERT INTO `halls` (`id`, `name`, `description`, `capacity`, `location`, `price_per_slot`, `status`) VALUES
(1, 'Grand Crystal Ballroom', 'A palatial ballroom with crystal chandeliers, a stage and a dance floor. Ideal for weddings and receptions.', 500, 'Adyar, Chennai', 45000, 'active'),
(2, 'Summit Conference Centre', 'Tiered seating, dual projectors and simultaneous interpretation booths for large conferences.', 300, 'Tidel Park Road, Chennai', 25000, 'active'),
(3, 'Lotus Workshop Hall', 'Flexible, bright hall with movable furniture for workshops, trainings and small seminars.', 80, 'RS Puram, Coimbatore', 12000, 'active'),
(4, 'Jasmine Terrace Garden', 'Open-air terrace with string lights and a garden backdrop for birthdays and cocktail evenings.', 120, 'Alwarpet, Chennai', 18000, 'active'),
(5, 'Executive Boardroom', 'A quiet, premium boardroom for leadership meetings, interviews and small seminars.', 40, 'Anna Nagar, Chennai', 8000, 'active'),
(6, 'Royal Palace Banquet', 'Traditional banquet hall with an in-house kitchen, bridal suites and generous parking.', 300, 'Race Course, Coimbatore', 30000, 'active')
ON DUPLICATE KEY UPDATE `name`=`name`;

-- Seed Hall Facilities
DELETE FROM `hall_facilities` WHERE `hall_id` IN (1, 2, 3, 4, 5, 6);
INSERT INTO `hall_facilities` (`hall_id`, `facility`) VALUES
(1, 'AC'), (1, 'Stage'), (1, 'Parking'), (1, 'Catering'), (1, 'Sound System'), (1, 'WiFi'),
(2, 'AC'), (2, 'Projector'), (2, 'WiFi'), (2, 'Sound System'), (2, 'Parking'),
(3, 'AC'), (3, 'Projector'), (3, 'WiFi'), (3, 'Whiteboards'),
(4, 'Open Air'), (4, 'Catering'), (4, 'Sound System'), (4, 'Parking'),
(5, 'AC'), (5, 'Projector'), (5, 'WiFi'), (5, 'Video Conferencing'),
(6, 'AC'), (6, 'Stage'), (6, 'Parking'), (6, 'Catering'), (6, 'Bridal Suite');

-- Seed Hall Images
DELETE FROM `hall_images` WHERE `hall_id` IN (1, 2, 3, 4, 5, 6);
INSERT INTO `hall_images` (`hall_id`, `image_url`) VALUES
(1, 'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80'),
(1, 'https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80'),
(1, 'https://images.unsplash.com/photo-1478147427282-58a87a120781?auto=format&fit=crop&w=1200&q=80'),
(2, 'https://images.unsplash.com/photo-1511578314322-379afb476865?auto=format&fit=crop&w=1200&q=80'),
(2, 'https://images.unsplash.com/photo-1540575467063-178a50c2df87?auto=format&fit=crop&w=1200&q=80'),
(3, 'https://images.unsplash.com/photo-1431540015161-0bf868a2d407?auto=format&fit=crop&w=1200&q=80'),
(3, 'https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1200&q=80'),
(4, 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=1200&q=80'),
(4, 'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80'),
(5, 'https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=1200&q=80'),
(5, 'https://images.unsplash.com/photo-1431540015161-0bf868a2d407?auto=format&fit=crop&w=1200&q=80'),
(6, 'https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80'),
(6, 'https://images.unsplash.com/photo-1478147427282-58a87a120781?auto=format&fit=crop&w=1200&q=80');

-- Seed Sample Bookings
INSERT INTO `bookings` (`id`, `hall_id`, `user_id`, `booking_date`, `slot`, `event_type`, `guests`, `purpose`, `status`, `created_at`) VALUES
(1, 1, 2, '2026-08-15', 'Evening', 'Wedding', 350, 'Wedding reception', 'approved', '2026-07-01T09:30:00.000Z'),
(2, 2, 3, '2026-09-10', 'Full day', 'Conference', 180, 'Annual tech conference', 'approved', '2026-07-20T11:00:00.000Z'),
(3, 1, 4, '2026-10-18', 'Evening', 'Wedding', 400, 'Wedding ceremony and dinner', 'approved', '2026-08-25T14:10:00.000Z'),
(4, 3, 2, '2026-10-05', 'Morning', 'Workshop', 60, 'Design thinking workshop', 'approved', '2026-09-02T08:45:00.000Z'),
(5, 4, 3, '2026-10-12', 'Afternoon', 'Birthday', 90, '50th birthday lunch', 'pending', '2026-09-20T16:20:00.000Z')
ON DUPLICATE KEY UPDATE `status`=`status`;
