package ru.mentee.power.orders.adapters.kafka;

import org.springframework.stereotype.Component;
import ru.mentee.power.orders.ports.incoming.ProcessOrderEventPort;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderEventMapper {

    public ProcessOrderEventPort.OrderEventCommand toCommand(OrderEventPayload payload, String eventId, Long kafkaOffset) {
        List<ProcessOrderEventPort.OrderEventCommand.OrderLine> lines = payload.lines().stream()
                .map(line -> new ProcessOrderEventPort.OrderEventCommand.OrderLine(
                        line.productId(),
                        line.quantity(),
                        line.price()
                ))
                .collect(Collectors.toList());

        return new ProcessOrderEventPort.OrderEventCommand(
                payload.orderId(),
                payload.customerId(),
                payload.region(),
                payload.priority(),
                payload.amount(),
                lines,
                eventId,
                kafkaOffset
        );
    }
}