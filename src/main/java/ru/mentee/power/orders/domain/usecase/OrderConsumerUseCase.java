package ru.mentee.power.orders.domain.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.mentee.power.orders.adapters.metrics.ConsumerMetricsRegistry;
import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.domain.model.OrderLine;
import ru.mentee.power.orders.domain.model.OrderStatus;
import ru.mentee.power.orders.ports.incoming.ProcessOrderEventPort;
import ru.mentee.power.orders.ports.outgoing.OrderPersistencePort;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConsumerUseCase implements ProcessOrderEventPort {

    private final OrderPersistencePort persistencePort;
    private final ConsumerMetricsRegistry metricsRegistry;

    @Override
    @Transactional
    public void process(OrderEventCommand command) {
        try {
            if (persistencePort.existsByEventId(command.eventId())) {
                log.info("Event already processed, skipping: eventId={}, orderId={}",
                        command.eventId(), command.orderId());
                return;
            }

            Order order = buildOrder(command);
            persistencePort.save(order, command.eventId(), command.kafkaOffset());

            metricsRegistry.recordSuccess(command.priority(), command.region());

            log.info("Order processed successfully: orderId={}, eventId={}",
                    order.getId(), command.eventId());

        } catch (Exception e) {
            metricsRegistry.recordFailure(command.priority());
            log.error("Failed to process order: eventId={}, orderId={}, error={}",
                    command.eventId(), command.orderId(), e.getMessage(), e);
            throw e;
        }
    }

    private Order buildOrder(OrderEventCommand command) {
        List<OrderLine> lines = command.lines().stream()
                .map(line -> OrderLine.builder()
                        .productId(line.productId())
                        .quantity(line.quantity())
                        .price(line.price())
                        .priority(command.priority().name())
                        .build())
                .collect(Collectors.toList());

        return Order.builder()
                .id(command.orderId())
                .customerId(command.customerId())
                .region(command.region())
                .priority(command.priority())
                .amount(command.amount())
                .status(OrderStatus.NEW)
                .lines(lines)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }
}