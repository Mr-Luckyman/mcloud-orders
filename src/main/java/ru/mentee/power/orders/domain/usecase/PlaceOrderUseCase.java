package ru.mentee.power.orders.domain.usecase;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.domain.model.OrderLine;
import ru.mentee.power.orders.domain.model.OrderStatus;
import ru.mentee.power.orders.ports.incoming.PlaceOrderCommand;
import ru.mentee.power.orders.ports.incoming.PlaceOrderPort;
import ru.mentee.power.orders.ports.incoming.PlaceOrderResult;
import ru.mentee.power.orders.ports.outgoing.OrderEventPort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PlaceOrderUseCase implements PlaceOrderPort {

    private final OrderEventPort orderEventPort;

    @Override
    public PlaceOrderResult placeOrder(PlaceOrderCommand command) {
        Order order = buildOrder(command);
        orderEventPort.publishOrderEvent(order);
        return new PlaceOrderResult(
                order.getId(),
                order.getStatus(),
                order.getAmount(),
                Instant.now()
        );
    }

    private Order buildOrder(PlaceOrderCommand command) {
        List<OrderLine> lines = command.lines().stream()
                .map(line -> new OrderLine(
                        line.productId(),
                        line.quantity(),
                        line.price(),
                        command.priority().name()
                ))
                .toList();

        return Order.builder()
                .id(UUID.randomUUID())
                .customerId(command.customerId())
                .amount(command.amount())
                .status(OrderStatus.NEW)
                .priority(command.priority())
                .region(command.region())
                .lines(lines)
                .createdAt(Instant.now())
                .build();
    }
}