package ru.mentee.power.orders.domain.model;

import java.time.LocalDateTime;
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
    private Double amount;
    private OrderStatus status;
    private List<OrderLine> lines;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
