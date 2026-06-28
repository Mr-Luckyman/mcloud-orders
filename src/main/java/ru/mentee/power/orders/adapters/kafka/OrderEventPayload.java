package ru.mentee.power.orders.adapters.kafka;

import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.domain.model.OrderStatus;
import ru.mentee.power.orders.domain.model.Priority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderEventPayload(
        UUID orderId,
        UUID customerId,
        String region,
        Double amount,
        OrderStatus status,
        Priority priority,
        List<OrderLinePayload> lines,
        LocalDateTime createdAt
) {
    public static OrderEventPayload from(Order order) {
        List<OrderLinePayload> linePayloads = order.getLines().stream()
                .map(line -> new OrderLinePayload(
                        line.getProductId(),
                        line.getQuantity(),
                        line.getPrice()
                ))
                .toList();

        return new OrderEventPayload(
                order.getId(),
                order.getCustomerId(),
                order.getRegion(),
                order.getAmount(),
                order.getStatus(),
                order.getPriority(),
                linePayloads,
                order.getCreatedAt()
        );
    }

    public record OrderLinePayload(
            UUID productId,
            Integer quantity,
            Double price
    ) {
    }
}