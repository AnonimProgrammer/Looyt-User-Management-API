package com.looyt.usermanagementservice.dto.request;

import com.looyt.usermanagementservice.model.enums.Role;
import com.looyt.usermanagementservice.model.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record UserFilterRequest(
        String search,
        Status status,
        Role role,
        @Min(0) @PositiveOrZero int page,
        @Min(1) int size
) {}
