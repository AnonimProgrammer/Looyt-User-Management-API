package com.looyt.usermanagementservice.dto.request;

import com.looyt.usermanagementservice.model.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
        @NotNull(message = "Role is required.")
        Role role
){}
