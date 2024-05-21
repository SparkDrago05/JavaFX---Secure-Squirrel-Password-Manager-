package com.password_manager;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.password_manager.Utils.EncryptionUtils;

import jakarta.persistence.EntityManagerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

public class DB {
    public static EntityManagerFactory entityManagerFactory;
    private static final String DB_PATH = "data.db";
    private static final String ENCRYPTION_KEY = "MyTestEncryptionKey12345"; // Must be 32 bytes for AES-256

    public static void connect() {
        try {
            decryptDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure(App.class.getResource("hibernate.cfg.xml")).build();

        try {
            entityManagerFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
            try {
                encryptDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void decryptDatabase() throws GeneralSecurityException, IOException {
        File encryptedFile = new File(DB_PATH + ".enc");
        File decryptedFile = new File(DB_PATH);
        if (encryptedFile.exists()) {
            EncryptionUtils.decrypt(ENCRYPTION_KEY, encryptedFile, decryptedFile);
        }
    }

    private static void encryptDatabase() throws GeneralSecurityException, IOException {
        File decryptedFile = new File(DB_PATH);
        File encryptedFile = new File(DB_PATH + ".enc");
        if (decryptedFile.exists()) {
            EncryptionUtils.encrypt(ENCRYPTION_KEY, decryptedFile, encryptedFile);
            Files.delete(Paths.get(DB_PATH));
        }
    }
}
