package ru.mentee.power.orders.adapters.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import ru.mentee.power.orders.adapters.metrics.ConsumerMetricsRegistry;
import ru.mentee.power.orders.adapters.metrics.ProducerMetricsRegistry;
import ru.mentee.power.orders.adapters.web.dto.OrderRequest;
import ru.mentee.power.orders.adapters.web.dto.OrderResponse;
import ru.mentee.power.orders.adapters.web.mapper.OrderMapper;
import ru.mentee.power.orders.ports.incoming.PlaceOrderCommand;
import ru.mentee.power.orders.ports.incoming.PlaceOrderPort;
import ru.mentee.power.orders.ports.incoming.PlaceOrderResult;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderPort placeOrderPort;
    private final ProducerMetricsRegistry metricsRegistry;
    private final ConsumerMetricsRegistry consumerMetricsRegistry;


    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        PlaceOrderCommand command = OrderMapper.toCommand(request);
        PlaceOrderResult result = placeOrderPort.placeOrder(command);
        return OrderMapper.toResponse(result);
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> response = new HashMap<>();

        // Общие метрики
        Map<String, Long> totals = new HashMap<>();
        totals.put("success", metricsRegistry.getTotalSuccess());
        totals.put("failure", metricsRegistry.getTotalFailure());
        response.put("totals", totals);

        // Детали по топикам
        Map<String, Map<String, Long>> topics = new HashMap<>();
        for (String topic : metricsRegistry.getSuccessSnapshot().keySet()) {
            Map<String, Long> topicMetrics = new HashMap<>();
            topicMetrics.put("success", metricsRegistry.getSuccessCount(topic));
            topicMetrics.put("failure", metricsRegistry.getFailureCount(topic));
            topics.put(topic, topicMetrics);
        }
        response.put("topics", topics);

        return response;
    }

    @GetMapping("/consumers/metrics")
    public Map<String, Object> getConsumerMetrics() {
        Map<String, Object> response = new HashMap<>();

        // Общие метрики
        Map<String, Long> totals = new HashMap<>();
        totals.put("processed", consumerMetricsRegistry.getTotalProcessed());
        totals.put("failed", consumerMetricsRegistry.getTotalFailed());
        response.put("totals", totals);

        // Детали по приоритетам
        Map<String, Long> byPriority = new HashMap<>();
        consumerMetricsRegistry.getProcessedByPriority()
                .forEach((priority, count) -> byPriority.put(priority.name(), count));
        response.put("byPriority", byPriority);

        // Детали по регионам
        response.put("byRegion", consumerMetricsRegistry.getProcessedByRegion());

        // Ошибки по приоритетам
        Map<String, Long> failedByPriority = new HashMap<>();
        consumerMetricsRegistry.getFailedByPriority()
                .forEach((priority, count) -> failedByPriority.put(priority.name(), count));
        response.put("failedByPriority", failedByPriority);

        return response;
    }
}