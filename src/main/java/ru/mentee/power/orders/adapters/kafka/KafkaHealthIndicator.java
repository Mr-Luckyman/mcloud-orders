package ru.mentee.power.orders.adapters.kafka;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaHealthIndicator implements HealthIndicator {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Health health() {
        try {
            // Проверяем, что брокер доступен
            var result = kafkaTemplate.getProducerFactory().createProducer()
                    .partitionsFor(KafkaConfig.ORDER_EVENTS_TOPIC);

            return Health.up()
                    .withDetail("broker", "available")
                    .withDetail("topic", KafkaConfig.ORDER_EVENTS_TOPIC)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
