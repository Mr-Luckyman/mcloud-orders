package ru.mentee.power.orders.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mentee.power.orders.adapters.metrics.ConsumerMetricsRegistry;
import ru.mentee.power.orders.domain.model.Priority;
import ru.mentee.power.orders.ports.incoming.ProcessOrderEventPort;
import ru.mentee.power.orders.ports.outgoing.OrderPersistencePort;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderConsumerUseCaseTest {

    @Mock
    private OrderPersistencePort persistencePort;

    @Mock
    private ConsumerMetricsRegistry metricsRegistry;

    @InjectMocks
    private OrderConsumerUseCase useCase;

    private ProcessOrderEventPort.OrderEventCommand command;

    @BeforeEach
    void setUp() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        command = new ProcessOrderEventPort.OrderEventCommand(
                orderId,
                customerId,
                "EU",
                Priority.HIGH,
                new BigDecimal("1200.50"),
                List.of(
                        new ProcessOrderEventPort.OrderEventCommand.OrderLine(
                                productId,
                                2,
                                600.25
                        )
                ),
                "test-event-id",
                0L
        );
    }

    @Test
    void shouldProcessNewOrder() {
        when(persistencePort.existsByEventId(anyString())).thenReturn(false);

        useCase.process(command);

        verify(persistencePort).save(any(), eq("test-event-id"), eq(0L));
        verify(metricsRegistry).recordSuccess(Priority.HIGH, "EU");
        verify(metricsRegistry, never()).recordFailure(any());
    }

    @Test
    void shouldSkipDuplicateEvent() {
        when(persistencePort.existsByEventId(anyString())).thenReturn(true);
        useCase.process(command);

        verify(persistencePort, never()).save(any(), anyString(), anyLong());
        verify(metricsRegistry, never()).recordSuccess(any(), anyString());
        verify(metricsRegistry, never()).recordFailure(any());
    }

    @Test
    void shouldRecordFailureOnError() {
        when(persistencePort.existsByEventId(anyString())).thenReturn(false);
        doThrow(new RuntimeException("Database error"))
                .when(persistencePort).save(any(), anyString(), anyLong());

        assertThrows(RuntimeException.class, () -> {
            useCase.process(command);
        });

        verify(metricsRegistry).recordFailure(Priority.HIGH);
        verify(metricsRegistry, never()).recordSuccess(any(), anyString());
    }
}