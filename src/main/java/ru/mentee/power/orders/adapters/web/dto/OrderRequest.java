package ru.mentee.power.orders.adapters.web.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record OrderRequest(
        UUID customerId,
        String region,
        String priority,
        Double amount,
        List<OrderLineDto> lines
) {
}
