package com.hallbooking.controller;

import com.hallbooking.dto.AuthResponse;
import com.hallbooking.dto.LoginRequest;
import com.hallbooking.dto.RegisterRequest;
import com.hallbooking.entity.User;
import com.hallbooking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return userService.findByCredentials(request.getEmail(), request.getPassword())
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(new AuthResponse(user, "Login successful")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse(null, "Invalid email or password")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String role = (request.getRole() != null && !request.getRole().isBlank())
                ? request.getRole().trim().toLowerCase()
                : "user";
        User newUser = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                role
        );
        User created = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(created, "Account registered successfully"));
    }
}
