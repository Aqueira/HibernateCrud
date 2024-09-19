package org.example.handlers.handlersImpl;


import org.example.HibernateContext;
import org.example.handlers.CustomersHandler;
import org.example.entities.Customer;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.logging.Logger;

public class CustomerHandlerImpl implements CustomersHandler {
    private static final Logger logger = Logger.getLogger("logger");
    private final HibernateContext hibernateContext;

    public CustomerHandlerImpl(HibernateContext hibernateContext) {
        this.hibernateContext = hibernateContext;
    }

    @Override
    public Optional<Customer> findCustomer(Integer number) {
        return hibernateContext.submit(session -> {
            Transaction transaction = session.beginTransaction();
            try {
                return Optional.ofNullable(
                    session.createQuery(
                            "SELECT customer " +
                                "FROM Customer customer " +
                                "LEFT JOIN FETCH customer.orders order " +
                                "WHERE customer.customerId = :customerId", Customer.class
                        )
                        .setParameter("customerId", number)
                        .getSingleResult()
                );
            } catch (Exception exception) {
                if (transaction != null) {
                    transaction.rollback();
                    logger.warning("Rolled back transaction with exception -> " + exception);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public void createCustomer(String name, String sector) {
        hibernateContext.execute(session -> {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(
                    Customer
                        .builder()
                        .name(name)
                        .sector(sector)
                        .build()
                );
                transaction.commit();
            } catch (Exception exception) {
                if (transaction != null) {
                    transaction.rollback();
                    logger.warning("Rolled back transaction with exception -> " + exception);
                }
            }
        });
    }

    @Override
    public void updateSector(Integer number, String sector) {
        hibernateContext.execute(session -> {
            Transaction transaction = session.beginTransaction();
            try {
                session
                    .createQuery("UPDATE Customer SET sector = :sector WHERE customerId = :customerId", null)
                    .setParameter("sector", sector)
                    .setParameter("customerId", number)
                    .executeUpdate();
                transaction.commit();
            } catch (Exception exception) {
                if (transaction != null) {
                    transaction.rollback();
                    logger.warning("Rolled back transaction with exception -> " + exception);
                }
            }
        });
    }

    @Override
    public void deleteCustomer(Integer number) {
        hibernateContext.execute(session -> {
            Transaction transaction = session.beginTransaction();
            try {
                session
                    .createQuery("DELETE FROM Customer WHERE customerId = :customerId", null)
                    .setParameter("customerId", number)
                    .executeUpdate();
                transaction.commit();
            } catch (Exception exception) {
                if (transaction != null) {
                    transaction.rollback();
                    logger.warning("Rolled back transaction with exception -> " + exception);
                }
            }
        });
    }
}
