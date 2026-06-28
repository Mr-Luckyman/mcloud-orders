package ru.mentee.power.orders.adapters.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderResponse(
        UUID orderId,
        String status,
        Double amount,
        LocalDateTime createdAt
) {
}
