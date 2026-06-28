package ru.mentee.power.orders.ports.incoming;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderLineCommand(
        UUID productId,
        Integer quantity,
        Double price
) {
}