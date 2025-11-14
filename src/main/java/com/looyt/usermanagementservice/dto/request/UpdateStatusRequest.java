package com.looyt.usermanagementservice.dto.request;

import com.looyt.usermanagementservice.model.enums.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "Status is required.")
        Status status
) {}
