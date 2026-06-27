package ru.mentee.power.orders.domain.usecase;

import org.springframework.stereotype.Component;

import ru.mentee.power.orders.domain.model.Order;
import ru.mentee.power.orders.ports.incoming.PlaceOrderPort;

@Component
public class PlaceOrderUseCase implements PlaceOrderPort {
    @Override
    public Order placeOrder(Order order) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
