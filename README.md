# Hall Booking System — Spring Boot Maven Backend

A Spring Boot 3 RESTful API backend for the **Hall Booking System**, configured to work with the Next.js frontend or any client application.

---

## 🚀 Features

- **Spring Boot 3.3.4 & Java 21**
- **Spring Data JPA & Hibernate** for database persistence
- **MySQL database** with automatic schema updates
- **Automatic Data Seeding**: Automatically pre-populates initial users (Admin + Users), halls with photos and facilities, and demo bookings on startup
- **Conflict-Free Booking System**: Validates against double-booking already approved slots
- **Global CORS Configured**: Seamless integration with Next.js running on `http://localhost:3000`
- **Full Compatibility with Next.js Frontend**: API runs on port 8080

---

## 📁 Project Structure

```
backend/
├── pom.xml                                      # Maven project descriptor
├── run.bat                                      # One-click Windows runner
├── run.ps1                                      # PowerShell runner script
├── setup-maven.ps1                              # Automatic portable Maven installer
├── README.md                                    # Documentation & API specs
└── src/
    ├── main/
    │   ├── resources/
    │   │   └── application.properties           # Database, port & JPA configuration
    │   └── java/
    │       └── com/hallbooking/
    │           ├── HallBookingApplication.java  # Main application entry point
    │           ├── config/
    │           │   ├── CorsConfig.java          # Cross-Origin Resource Sharing
    │           │   └── DataInitializer.java     # Seeds mock data on first launch
    │           ├── controller/
    │           │   ├── AuthController.java      # /auth/login & /auth/register
    │           │   ├── BookingController.java   # /bookings REST endpoints
    │           │   ├── HallController.java      # /halls REST endpoints
    │           │   └── UserController.java      # /users REST endpoints
    │           ├── dto/
    │           │   ├── AuthResponse.java
    │           │   ├── LoginRequest.java
    │           │   └── RegisterRequest.java
    │           ├── entity/
    │           │   ├── Booking.java             # JPA entity for bookings
    │           │   ├── Hall.java                # JPA entity for halls
    │           │   └── User.java                # JPA entity for users
    │           ├── exception/
    │           │   ├── GlobalExceptionHandler.java
    │           │   └── ResourceNotFoundException.java
    │           ├── repository/
    │           │   ├── BookingRepository.java
    │           │   ├── HallRepository.java
    │           │   └── UserRepository.java
    │           └── service/
    │               ├── BookingService.java
    │               ├── HallService.java
    │               └── UserService.java
    └── test/
        └── java/com/hallbooking/
            └── HallBookingApplicationTests.java
```

---

## ⚡ How to Run

### Method 1: One-Click Run (Windows)
Double-click `run.bat` inside the `backend` folder, or in Command Prompt / Terminal:
```cmd
run.bat
```
*(Or in PowerShell: `.\run.ps1`)*

> **Note:** If you don't have Maven installed globally, the script automatically uses the bundled portable Maven in `.tools/apache-maven-3.9.9`.

> **Java requirement:** Java 21 or newer. The Windows runner scripts prefer an installed JDK under `.tools/jdk-25`; otherwise, a compatible JDK must be available on `PATH`.

### Method 2: Standard Maven CLI
If you have Maven installed on your PATH, ensure `JAVA_HOME` points to JDK 25 before running Maven. On Windows, the project wrapper selects the project-local JDK automatically:
```bash
# Windows PowerShell
.\mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

### Method 3: In VS Code or IntelliJ IDEA
1. Open the `backend` folder in VS Code or IntelliJ IDEA.
2. Ensure you have the **Extension Pack for Java** installed.
3. Open `HallBookingApplication.java` and click **Run** (or `Shift + F10` in IntelliJ).

---

## 🌐 Endpoints & API Reference

Default Base URL: `http://localhost:8080`

### 1. Users (`/users`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/users` | List all users |
| `GET` | `/users?email={email}` | Find user by email |
| `GET` | `/users?email={email}&password={pwd}` | Find user by credentials (login verification) |
| `GET` | `/users/{id}` | Get user by ID |
| `POST` | `/users` | Create / register a user |
| `PATCH` | `/users/{id}` | Update user fields (name, phone, password, role) |
| `DELETE` | `/users/{id}` | Delete user |

### 2. Halls (`/halls`)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/halls` | List all available halls |
| `GET` | `/halls/{id}` | Get hall details by ID |
| `POST` | `/halls` | Create a new hall (Admin) |
| `PATCH` | `/halls/{id}` | Update hall details (Admin) |
| `DELETE` | `/halls/{id}` | Delete a hall (Admin) |

### 3. Bookings (`/bookings`)
| Method | Endpoint | Query Filters / Description |
|---|---|---|
| `GET` | `/bookings` | List all bookings |
| `GET` | `/bookings?userId={userId}` | Filter bookings by user |
| `GET` | `/bookings?hallId={id}&status=approved` | Filter approved slots for a hall |
| `GET` | `/bookings?status={status}` | Filter by status (`pending`, `approved`, `rejected`, `cancelled`) |
| `GET` | `/bookings/{id}` | Get booking details by ID |
| `POST` | `/bookings` | Create a new booking request |
| `PATCH` | `/bookings/{id}` | Update status / edit booking |
| `DELETE` | `/bookings/{id}` | Cancel / delete booking |

### 4. Dedicated Auth API (`/auth`)
| Method | Endpoint | Request Body | Description |
|---|---|---|---|
| `POST` | `/auth/login` | `{ "email": "...", "password": "..." }` | Login user & return user profile |
| `POST` | `/auth/register` | `{ "name": "...", "email": "...", "password": "...", "phone": "..." }` | Register new user |

---

## 🔑 Pre-Seeded Demo Accounts

When the application starts, these accounts are already created:

| Role | Email | Password | Name |
|---|---|---|---|
| **Admin** | `admin@hallbook.com` | `Admin@123` | Admin |
| **User** | `praveen12@gmail.com` | `User@123` | Praveen |
| **User** | `arjun@example.com` | `User@123` | Arjun Mehta |
| **User** | `kavya@example.com` | `User@123` | Kavya Nair |

---

## 🛠 Legacy H2 Console (not enabled)

The current configuration uses MySQL, so the H2 console below is unavailable unless you explicitly configure the application to use H2.
- **URL**: `http://localhost:4000/h2-console`
- **JDBC URL**: `jdbc:h2:mem:hallbookingdb`
- **User Name**: `sa`
- **Password**: *(leave empty)*

---

## 🐬 Active MySQL Configuration

MySQL is the active database. Its connection settings are configured in `src/main/resources/application.properties`.
1. Open [`src/main/resources/application.properties`](src/main/resources/application.properties).
2. Uncomment the MySQL section:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hall_booking_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_mysql_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   ```
3. Run the application; Hibernate will automatically create the database and tables.
