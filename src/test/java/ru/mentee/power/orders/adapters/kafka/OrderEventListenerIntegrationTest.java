package ru.mentee.power.orders.adapters.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.kafka.test.context.EmbeddedKafka;

import org.springframework.test.context.ActiveProfiles;

import ru.mentee.power.orders.adapters.persistence.OrderRepository;
import ru.mentee.power.orders.domain.model.OrderStatus;
import ru.mentee.power.orders.domain.model.Priority;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"orders.priority.high", "orders.priority.normal", "orders.priority.low"})
class OrderEventListenerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, OrderEventPayload> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldConsumeOrderEventAndSaveToDatabase() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        OrderEventPayload payload = new OrderEventPayload(
                orderId,
                customerId,
                "EU",
                new BigDecimal("1200.50"),
                OrderStatus.NEW,
                Priority.HIGH,
                List.of(
                        new OrderEventPayload.OrderLinePayload(
                                productId,
                                2,
                                600.25
                        )
                ),
                Instant.now()
        );

        kafkaTemplate.send("orders.priority.high", "EU", payload);

        await().atMost(10, SECONDS).untilAsserted(() -> {
            assertThat(orderRepository.existsByEventId(orderId + "-0")).isTrue();
        });
    }
}