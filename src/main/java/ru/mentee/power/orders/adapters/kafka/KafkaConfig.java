package ru.mentee.power.orders.adapters.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    public static final String ORDER_EVENTS_TOPIC = "orders-events";

    @Bean
    public NewTopic orderEventTopic() {
        return new NewTopic(ORDER_EVENTS_TOPIC, 10, (short) 1);
    }

    // TODO: добавить настройки ProducerFactory, ConsumerFactory
    // TODO: добавить сериализаторы/десериализаторы
}
