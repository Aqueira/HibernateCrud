package org.example.handlers.handlersImpl;

import org.example.HibernateContext;
import org.example.handlers.OrdersHandler;
import org.example.entities.Customer;
import org.example.entities.LineItem;
import org.example.entities.Order;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class OrderHandlerImpl implements OrdersHandler {
    private static final Logger logger = Logger.getLogger("logger");
    private final HibernateContext hibernateContext;

    public OrderHandlerImpl(HibernateContext hibernateContext) {
        this.hibernateContext = hibernateContext;
    }

    @Override
    public void placeOrder(Customer customer, String deliverTo, List<LineItem> items) {
        hibernateContext.execute(session -> {
            Transaction transaction = session.beginTransaction();
            try {
                Order order = Order.builder().deliverTo(deliverTo).customer(customer).build();
                session.persist(order);
                for (LineItem item : items) {
                    item.setOrder(order);
                    session.persist(item);
                }
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
    public Optional<Order> findOrder(Integer number) {
        return Optional.ofNullable(hibernateContext.submit(session -> session.createQuery(
                "SELECT o " +
                    "FROM Order o " +
                    "LEFT JOIN FETCH o.lineItems li " +
                    "LEFT JOIN FETCH o.customer c " +
                    "WHERE o.orderId = :orderId", Order.class
            )
             .setParameter("orderId", number)
             .getSingleResult()));
    }

    @Override
    public void deleteOrder(Integer number) {
        hibernateContext.execute(session -> {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM LineItem lineItem WHERE lineItem.order.orderId = :number", null)
                    .setParameter("number", number)
                    .executeUpdate();

                session.createQuery("DELETE FROM Order order WHERE order.orderId = :number", null)
                    .setParameter("number", number)
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

