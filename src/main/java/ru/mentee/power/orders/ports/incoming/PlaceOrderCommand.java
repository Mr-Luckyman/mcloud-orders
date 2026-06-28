package ru.mentee.power.orders.ports.incoming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ru.mentee.power.orders.domain.model.Priority;

import java.util.List;
import java.util.UUID;

@Builder
public record PlaceOrderCommand(
        @NotNull UUID customerId,
        @NotBlank String region,
        @Positive Double amount,
        @NotNull Priority priority,
        @NotEmpty List<OrderLineCommand> lines
) {
}