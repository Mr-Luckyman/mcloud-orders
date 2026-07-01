package ru.mentee.power.orders.adapters.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.mentee.power.orders.adapters.persistence.jpa.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
    boolean existsByEventId(String eventId);
}
