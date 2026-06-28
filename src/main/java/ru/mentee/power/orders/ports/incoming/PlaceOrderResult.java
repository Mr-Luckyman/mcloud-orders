package ru.mentee.power.orders.ports.incoming;

import ru.mentee.power.orders.domain.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record PlaceOrderResult(
        UUID orderId,
        OrderStatus status,
        Double amount,
        LocalDateTime dispatchedAt
) {
}