package com.lgsoftworks.plan.application.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PlanResponse(
        UUID id,
        String name,
        String description,
        Integer durationDays,
        BigDecimal price,
        LocalDateTime createdAt
) {
}