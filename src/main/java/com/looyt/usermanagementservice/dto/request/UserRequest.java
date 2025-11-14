package com.looyt.usermanagementservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Name is required.")
        @Size(
                min = 2, max = 100,
                message = "Name must consist of at least 2 and at most 100 characters."
        )
        String name,

        @NotBlank(message = "Email is required.")
        @Email(
                message = "Invalid email address.",
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        String email,

        @NotBlank(message = "Phone number is required.")
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Invalid phone number format."
        )
        String phoneNumber
) {}

