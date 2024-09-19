package org.example.handlers;

import org.example.entities.Customer;
import org.example.entities.LineItem;
import org.example.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrdersHandler {
    void placeOrder(Customer customer, String deliverTo, List<LineItem> items);

    Optional<Order> findOrder(Integer number);

    void deleteOrder(Integer number);
}

