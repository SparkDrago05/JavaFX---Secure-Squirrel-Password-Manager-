package com.password_manager.Controllers;

import java.util.List;

import com.password_manager.App;
import com.password_manager.DB;
import com.password_manager.Models.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import com.password_manager.Controllers.MainView;

public class SignInController implements Initializable {
    @FXML
    private TextField confirmPasswordInputRegister;

    @FXML
    private VBox loginBox;

    @FXML
    private TextField passwordInputLogin;

    @FXML
    private TextField passwordInputRegister;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox registerBox;

    @FXML
    private TextField usernameInputLogin;

    @FXML
    private TextField usernameInputRegister;

    private Parent mainViewContent;
    private MainView mainViewController;

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        loginBox.setVisible(true);
        registerBox.setVisible(false);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/main_view.fxml"));
        try {
            this.mainViewContent = loader.load();
            this.mainViewController = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToLogin(MouseEvent event) {
        loginBox.setVisible(true);
        registerBox.setVisible(false);
        this.messageLabel.setText(null);
        this.messageLabel.setVisible(false);
    }

    @FXML
    void switchToRegister(MouseEvent event) {
        loginBox.setVisible(false);
        registerBox.setVisible(true);
        this.messageLabel.setText(null);
        this.messageLabel.setVisible(false);
    }

    @FXML
    void signup(ActionEvent event) {
        String username = usernameInputRegister.getText().trim();
        String password = passwordInputRegister.getText();
        String confirmPassword = confirmPasswordInputRegister.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match");
            return;
        }
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        try {
            entityManager = DB.entityManagerFactory.createEntityManager();
            User existingUser = entityManager.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                    .setParameter("name", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingUser == null) {
                User newUser = new User(username, password);
                persistUser(entityManager, newUser);
                showMessage("User created successfully");
            } else {
                showMessage("User already exists");
            }
        } catch (Exception e) {
            showMessage("An error occurred while creating the user");
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    private void persistUser(EntityManager entityManager, User user) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(user);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw e;
        }
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true);
    }

    @FXML
    void signIn() {
        String username = usernameInputLogin.getText().trim();
        String password = passwordInputLogin.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields");
            return;
        }

        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            List<User> users = entityManager.createQuery("from User").getResultList();
            users.forEach(user -> {
                if (user.getName().equals(username)
                        && user.getPassword().equals(password)) {
                    showMessage("Login Successful");
                    App.currentUser = user.getId();
                    Scene scene = new Scene(mainViewContent, 900, 600);
                    for (String stylesheetName : App.getStylesheetNames()) {
                        scene.getStylesheets().add(App.class.getResource("css/" + stylesheetName).toExternalForm());
                    }
                    mainViewController.switchView("dashboard");
                    App.getStage().setScene(scene);
                } else {
                    showMessage("Login Failed");
                }
            });

            if (users.isEmpty()) {
                showMessage("Login Failed");
            }
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

}
