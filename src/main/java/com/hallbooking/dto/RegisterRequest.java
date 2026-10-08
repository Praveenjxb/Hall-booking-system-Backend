package com.hallbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 254, message = "Email must be at most 254 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 72, message = "Password must be between 6 and 72 characters")
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^(?=(?:\\D*\\d){7,15}\\D*$)\\+?[0-9\\s().-]+$",
            message = "Enter a valid mobile number with 7 to 15 digits")
    @Size(max = 20, message = "Phone must be at most 20 characters")
    private String phone;

    @jakarta.validation.constraints.Pattern(
            regexp = "(?i)^(user|admin)?$",
            message = "Role must be user or admin")
    private String role; // defaults to "user" if null

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String password, String phone, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
