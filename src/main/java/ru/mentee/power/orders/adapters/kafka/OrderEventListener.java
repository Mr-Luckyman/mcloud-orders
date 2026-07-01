package ru.mentee.power.orders.adapters.kafka;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.mentee.power.orders.ports.incoming.ProcessOrderEventPort;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final ProcessOrderEventPort eventPort;
    private final OrderEventMapper mapper;

    @KafkaListener(
            topics = {"orders.priority.high", "orders.priority.normal", "orders.priority.low"},
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            @Payload @NonNull OrderEventPayload payload,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key,
            @Header(KafkaHeaders.OFFSET) Long offset,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            Acknowledgment acknowledgment
    ) {
        log.info("Received order event: orderId={}, key={}, partition={}, offset={}",
                payload.orderId(), key, partition, offset);

        try {
            String eventId = payload.orderId() + "-" + offset;
            var command = mapper.toCommand(payload, eventId, offset);
            eventPort.process(command);

            if (acknowledgment != null) {
                acknowledgment.acknowledge();
            }

            log.info("Order event processed successfully: orderId={}, offset={}",
                    payload.orderId(), offset);

        } catch (Exception e) {
            log.error("Error processing order event: orderId={}, offset={}, error={}",
                    payload.orderId(), offset, e.getMessage(), e);
            // TODO: отправить в DLQ (будет в MKAFKA-5)
            throw e; // временно пробрасываем, чтобы Kafka повторила
        }
    }
}
