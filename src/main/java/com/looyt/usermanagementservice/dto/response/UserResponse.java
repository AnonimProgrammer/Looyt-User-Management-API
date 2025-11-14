package com.looyt.usermanagementservice.dto.response;

import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        Role role,
        Status status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
