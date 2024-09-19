package org.example;

import org.example.entities.Customer;
import org.example.entities.LineItem;
import org.example.entities.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

public final class HibernateContext implements AutoCloseable {
    private final SessionFactory sessionFactory;

    public HibernateContext(){
        Configuration configuration = hibernateConfiguration();
        sessionFactory = configuration.buildSessionFactory();
    }

    private Configuration hibernateConfiguration(){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(LineItem.class);
        configuration.configure();

        return configuration;
    }

    public void execute(Consumer<Session> consumer) {
        try (Session session = sessionFactory.openSession()) {
            consumer.accept(session);
        }
    }

    public <R> R submit(Function<Session, R> function){
        try (Session session = sessionFactory.openSession()) {
            return function.apply(session);
        }
    }

    @Override
    public void close(){
        sessionFactory.close();
    }
}
