package org.example.handlers;

import org.example.entities.Customer;

import java.util.Optional;

public interface CustomersHandler {
    Optional<Customer> findCustomer(Integer number);

    void createCustomer(String name, String sector);

    void updateSector(Integer number, String sector);

    void deleteCustomer(Integer number);
}

