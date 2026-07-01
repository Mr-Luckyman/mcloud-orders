package ru.mentee.power.orders.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    private OrderStatus status;
    private String region;
    private Priority priority;
    private List<OrderLine> lines;
    private Instant createdAt;
    private Instant updatedAt;
    private String eventId;
    private Long kafkaOffset;
}
