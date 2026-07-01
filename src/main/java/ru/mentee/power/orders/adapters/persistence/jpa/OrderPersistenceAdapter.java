package ru.mentee.power.orders.adapters.persistence.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import ru.mentee.power.orders.adapters.persistence.OrderRepository;
import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.ports.outgoing.OrderPersistencePort;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderRepository orderRepository;
    private final OrderPersistenceMapper mapper;

    @Override
    public void save(Order order, String eventId, Long kafkaOffset) {
        OrderEntity entity = mapper.toEntity(order, eventId, kafkaOffset);

        if (order.getLines() != null) {
            order.getLines().forEach(line -> {
                OrderLineEntity lineEntity = mapper.toLineEntity(line);
                lineEntity.setOrder(entity);
            });
        }
        orderRepository.save(entity);
    }

    @Override
    public boolean existsByEventId(String eventId) {
        return orderRepository.existsByEventId(eventId);
    }
}
