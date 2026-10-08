package com.hallbooking.controller;

import com.hallbooking.entity.User;
import com.hallbooking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Supports:
     * - GET /users
     * - GET /users?email={email}
     * - GET /users?email={email}&password={password}
     * Returning a list is fully compatible with Next.js json-server expectations.
     */
    @GetMapping
    public List<User> getUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password) {

        if (email != null && password != null) {
            return userService.findByCredentials(email, password)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        if (email != null) {
            return userService.findByEmail(email)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        }

        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public User patchUser(@PathVariable Long id, @RequestBody User patch) {
        return userService.updateUser(id, patch);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User patch) {
        return userService.updateUser(id, patch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
