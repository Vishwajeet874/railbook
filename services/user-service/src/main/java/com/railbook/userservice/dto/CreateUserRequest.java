package com.railbook.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @NotBlank(message = "Phone is required")
        @Size(min = 10, max = 20, message = "Phone must contain 10 to 20 characters")
        String phone,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 50, message = "Password must contain at least 8 characters")
        String password
) {}
