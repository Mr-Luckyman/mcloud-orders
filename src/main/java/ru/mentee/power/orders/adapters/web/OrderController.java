package ru.mentee.power.orders.adapters.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.mentee.power.orders.adapters.web.dto.OrderRequest;
import ru.mentee.power.orders.adapters.web.dto.OrderResponse;
import ru.mentee.power.orders.ports.incoming.PlaceOrderPort;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final PlaceOrderPort placeOrderPort;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable String orderId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
