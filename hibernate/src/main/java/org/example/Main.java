package org.example;


import org.example.handlers.CustomersHandler;
import org.example.handlers.OrdersHandler;
import org.example.handlers.handlersImpl.CustomerHandlerImpl;
import org.example.handlers.handlersImpl.OrderHandlerImpl;
import org.example.entities.Customer;
import org.example.entities.LineItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Main {
    public static void main(String[] args) {
        try(HibernateContext hibernateContext = new HibernateContext()){
            CustomersHandler customerImpl = new CustomerHandlerImpl(hibernateContext);
            OrdersHandler orderImpl = new OrderHandlerImpl(hibernateContext);
            customerImpl.createCustomer("Gitler", "Poland");
            Optional<Customer> customer = customerImpl.findCustomer(1);
            List<LineItem> list = new ArrayList<>();
            LineItem item1 = LineItem
                .builder()
                .productName("PenisXXL")
                .price(30.35)
                .quantity(30)
                .build();

            LineItem item2 = LineItem
                .builder()
                .productName("PenisL")
                .price(30.90)
                .quantity(25)
                .build();

            list.add(item1);
            list.add(item2);
            orderImpl.placeOrder(customer.get(),"PENISKA", list);
            orderImpl.deleteOrder(3);
            customerImpl.deleteCustomer(21);
            customer.ifPresent(value -> System.out.println(value.getOrders()));

        }
    }
}
