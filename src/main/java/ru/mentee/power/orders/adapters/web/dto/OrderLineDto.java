package ru.mentee.power.orders.adapters.web.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderLineDto(
        UUID productId,
        Integer quantity,
        Double price,
        String priority
) {
}
