package ru.mentee.power.orders.ports.incoming;

public interface PlaceOrderPort {
    PlaceOrderResult placeOrder(PlaceOrderCommand order);
}
