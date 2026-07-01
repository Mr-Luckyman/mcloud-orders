package ru.mentee.power.orders.ports.incoming;

import ru.mentee.power.orders.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PlaceOrderResult(
        UUID orderId,
        OrderStatus status,
        BigDecimal amount,
        Instant dispatchedAt
) {
}