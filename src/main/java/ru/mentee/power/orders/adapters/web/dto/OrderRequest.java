package ru.mentee.power.orders.adapters.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderRequest(
        @NotNull UUID customerId,
        @NotBlank String region,
        @NotBlank String priority,
        @Positive BigDecimal amount,
        @Size(min = 1) List<OrderLineDto> lines
) {
}
