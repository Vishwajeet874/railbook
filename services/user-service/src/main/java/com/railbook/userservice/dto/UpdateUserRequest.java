package com.railbook.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @Size(min = 10, max = 20, message = "Phone must contain 10 to 20 characters")
        String phone
) {}
