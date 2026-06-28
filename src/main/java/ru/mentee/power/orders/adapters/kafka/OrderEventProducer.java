package ru.mentee.power.orders.adapters.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mentee.power.orders.adapters.metrics.ProducerMetricsRegistry;
import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.ports.outgoing.OrderEventPort;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer implements OrderEventPort {

    private final KafkaTemplate<String, OrderEventPayload> kafkaTemplate;
    private final KafkaTopicResolver topicResolver;
    private final ProducerMetricsRegistry metricsRegistry;

    @Override
    public void publishOrderEvent(Order order) {
        // 1. Определяем топик по приоритету
        String topic = topicResolver.resolveTopic(order.getPriority());

        // 2. Создаём payload из доменной сущности
        OrderEventPayload payload = OrderEventPayload.from(order);

        // 3. Создаём запись с ключом = регион (для партиционирования)
        ProducerRecord<String, OrderEventPayload> rec =
                new ProducerRecord<>(topic, order.getRegion(), payload);

        // 4. Отправляем асинхронно
        CompletableFuture<SendResult<String, OrderEventPayload>> future =
                kafkaTemplate.send(rec);

        // 5. Обрабатываем результат
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Order event sent successfully: orderId={}, topic={}, partition={}, offset={}",
                        order.getId(), topic, result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                metricsRegistry.recordSuccess(topic);
            } else {
                log.error("Failed to send order event: orderId={}, topic={}, error={}",
                        order.getId(), topic, ex.getMessage(), ex);
                metricsRegistry.recordFailure(topic);
            }
        });
    }
}