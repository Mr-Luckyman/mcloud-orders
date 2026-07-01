package ru.mentee.power.orders.adapters.metrics;

import org.springframework.stereotype.Component;
import ru.mentee.power.orders.domain.model.Priority;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ConsumerMetricsRegistry {

    // Счётчики по приоритетам
    private final Map<Priority, AtomicLong> processedByPriority = new ConcurrentHashMap<>();
    private final Map<Priority, AtomicLong> failedByPriority = new ConcurrentHashMap<>();

    // Счётчики по регионам
    private final Map<String, AtomicLong> processedByRegion = new ConcurrentHashMap<>();

    // Общие счётчики
    private final AtomicLong totalProcessed = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    public void recordSuccess(Priority priority, String region) {
        // По приоритету
        processedByPriority.computeIfAbsent(priority, k -> new AtomicLong()).incrementAndGet();

        // По региону
        processedByRegion.computeIfAbsent(region, k -> new AtomicLong()).incrementAndGet();

        // Общий
        totalProcessed.incrementAndGet();
    }

    public void recordFailure(Priority priority) {
        failedByPriority.computeIfAbsent(priority, k -> new AtomicLong()).incrementAndGet();
        totalFailed.incrementAndGet();
    }

    // Геттеры для метрик
    public Map<Priority, Long> getProcessedByPriority() {
        return snapshot(processedByPriority);
    }

    public Map<Priority, Long> getFailedByPriority() {
        return snapshot(failedByPriority);
    }

    public Map<String, Long> getProcessedByRegion() {
        return snapshot(processedByRegion);
    }

    public long getTotalProcessed() {
        return totalProcessed.get();
    }

    public long getTotalFailed() {
        return totalFailed.get();
    }

    private <K> Map<K, Long> snapshot(Map<K, AtomicLong> counters) {
        Map<K, Long> result = new ConcurrentHashMap<>();
        counters.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }
}