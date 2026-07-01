package ru.mentee.power.orders.adapters.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderResponse(
        UUID orderId,
        String status,
        BigDecimal amount,
        Instant createdAt
) {
}
