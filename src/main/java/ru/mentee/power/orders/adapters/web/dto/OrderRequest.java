package ru.mentee.power.orders.adapters.web.dto;

import java.util.List;
import java.util.UUID;

public record OrderRequest(
        UUID customerId,
        Double amount,
        List<OrderLineDto> lines
) {
    public static class OrderLineDto {
        private UUID productId;
        private Integer quantity;
        private Double amount;
        private String priority;
    }
}
