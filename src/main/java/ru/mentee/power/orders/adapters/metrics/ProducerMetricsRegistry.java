package ru.mentee.power.orders.adapters.metrics;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProducerMetricsRegistry {

    private final Map<String, AtomicLong> successCounters = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> failureCounters = new ConcurrentHashMap<>();

    public void recordSuccess(String topic) {
        successCounters.computeIfAbsent(topic, k -> new AtomicLong()).incrementAndGet();
    }

    public void recordFailure(String topic) {
        failureCounters.computeIfAbsent(topic, k -> new AtomicLong()).incrementAndGet();
    }

    public long getSuccessCount(String topic) {
        return successCounters.getOrDefault(topic, new AtomicLong(0)).get();
    }

    public long getFailureCount(String topic) {
        return failureCounters.getOrDefault(topic, new AtomicLong(0)).get();
    }

    public Map<String, Long> getSuccessSnapshot() {
        return snapshot(successCounters);
    }

    public Map<String, Long> getFailureSnapshot() {
        return snapshot(failureCounters);
    }

    public long getTotalSuccess() {
        return sum(successCounters);
    }

    public long getTotalFailure() {
        return sum(failureCounters);
    }

    private Map<String, Long> snapshot(Map<String, AtomicLong> counters) {
        Map<String, Long> result = new ConcurrentHashMap<>();
        counters.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    private long sum(Map<String, AtomicLong> counters) {
        return counters.values().stream()
                .mapToLong(AtomicLong::get)
                .sum();
    }
}
