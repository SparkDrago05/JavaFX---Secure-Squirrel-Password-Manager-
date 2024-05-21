package com.password_manager;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import jakarta.persistence.EntityManagerFactory;

public class DB {
    public static EntityManagerFactory entityManagerFactory;

    public static void connect() {
        // A SessionFactory is set up once for an Application
        // configures settings from hibernate.cfg.xml
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(App.class.getResource("hibernate.cfg.xml")).build();

        try {
            entityManagerFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }
}
