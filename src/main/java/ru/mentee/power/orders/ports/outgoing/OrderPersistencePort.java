package ru.mentee.power.orders.ports.outgoing;

import ru.mentee.power.orders.domain.model.Order;

public interface OrderPersistencePort {
    void save(Order order, String eventId, Long kafkaOffset);
    boolean existsByEventId(String eventId);
}