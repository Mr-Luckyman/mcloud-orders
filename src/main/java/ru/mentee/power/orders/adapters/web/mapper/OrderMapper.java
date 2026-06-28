package ru.mentee.power.orders.adapters.web.mapper;

import java.util.List;

import ru.mentee.power.orders.adapters.web.dto.OrderRequest;
import ru.mentee.power.orders.adapters.web.dto.OrderResponse;
import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.domain.model.Priority;
import ru.mentee.power.orders.ports.incoming.OrderLineCommand;
import ru.mentee.power.orders.ports.incoming.PlaceOrderCommand;
import ru.mentee.power.orders.ports.incoming.PlaceOrderResult;

public class OrderMapper {
    public static Order toDomain(OrderRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static PlaceOrderCommand toCommand(OrderRequest request) {
        List<OrderLineCommand> lines = request.lines().stream()
                .map(line -> OrderLineCommand.builder()
                        .productId(line.productId())
                        .quantity(line.quantity())
                        .price(line.price())
                        .build())
                .toList();

        Priority priority = Priority.valueOf(request.priority().toUpperCase());

        return PlaceOrderCommand.builder()
                .customerId(request.customerId())
                .region(request.region())
                .amount(request.amount())
                .priority(priority)
                .lines(lines)
                .build();
    }

    public static OrderResponse toResponse(PlaceOrderResult result) {
        return new OrderResponse(
                result.orderId(),
                result.status().name(),
                result.amount(),
                result.dispatchedAt()
        );
    }
}