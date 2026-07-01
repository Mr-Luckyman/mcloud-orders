package ru.mentee.power.orders.ports.incoming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotEmpty;
import ru.mentee.power.orders.domain.model.Priority;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProcessOrderEventPort {
    void process(@Valid OrderEventCommand command);

    record OrderEventCommand(
            @NotNull UUID orderId,
            @NotNull UUID customerId,
            @NotBlank String region,
            @NotNull Priority priority,
            @Positive BigDecimal amount,
            @NotEmpty List<OrderLine> lines,
            @NotBlank String eventId,
            @NotNull Long kafkaOffset
    ) {
        public record OrderLine(
                UUID productId,
                Integer quantity,
                Double price
        ) {}
    }
}