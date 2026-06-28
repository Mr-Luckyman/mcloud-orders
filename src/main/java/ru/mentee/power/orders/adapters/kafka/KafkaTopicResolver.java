package ru.mentee.power.orders.adapters.kafka;

import org.springframework.stereotype.Component;
import ru.mentee.power.orders.domain.model.Priority;

@Component
public class KafkaTopicResolver {

    private static final String TOPIC_PREFIX = "orders.priority";

    public String resolveTopic(Priority priority) {
        return switch (priority) {
            case HIGH -> TOPIC_PREFIX + ".high";
            case NORMAL -> TOPIC_PREFIX + ".normal";
            case LOW -> TOPIC_PREFIX + ".low";
        };
    }
}
