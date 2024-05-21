package com.password_manager;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import fr.brouillard.oss.cssfx.CSSFX;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Stage stage;
    private static Scene scene;
    public static int currentUser = 1;
    // hostServices
    public static HostServices hostServices;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/auth2.fxml"));
        // FXMLLoader fxmlLoader = new
        // FXMLLoader(App.class.getResource("fxml/main_view.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 760, 470);
        // scene = new Scene(root, 900, 600);
        // scene.getStylesheets().add(App.class.getResource("authentication.css").toExternalForm());
        // scene.getStylesheets().add(App.class.getResource("main_view.css").toExternalForm());
        for (String stylesheetName : getStylesheetNames()) {
            scene.getStylesheets().add(App.class.getResource("css/" + stylesheetName).toExternalForm());
        }
        stage.setScene(scene);
        stage.show();
        this.stage = stage;
        stage.setTitle("Password Manager");

        // hostServices
        hostServices = getHostServices();
    }

    public static void main(String[] args) {
        CSSFX.start();
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        // Application.setUserAgentStylesheet(new
        // PrimerDark().getUserAgentStylesheet());
        System.out.println("Application Started");
        launch();
    }

    public static ArrayList<String> getStylesheetNames() {
        ArrayList<String> stylesheetNames = new ArrayList<String>();
        stylesheetNames.add("authentication.css");
        stylesheetNames.add("main_view.css");
        stylesheetNames.add("dashboard.css");
        stylesheetNames.add("kanban_view.css");
        stylesheetNames.add("form.css");
        stylesheetNames.add("tag.css");
        stylesheetNames.add("new_tag_dialog.css");
        return stylesheetNames;
    }

    public static Stage getStage() {
        return stage;
    }

    /* Method to CREATE an employee in the database */
    // public int addEmployee(String name, int salary) {
    // EntityManager entityManager = entityManagerFactory.createEntityManager();
    // EntityTransaction entityTransaction = null;
    // int employeeID = 0;
    // try {
    // entityTransaction = entityManager.getTransaction();
    // entityTransaction.begin();
    // Employee employee = new Employee(name, salary);
    // entityManager.persist(employee);
    // entityTransaction.commit();
    // employeeID = employee.getId();
    // } catch (Exception e) {
    // if (entityTransaction != null) {
    // entityTransaction.rollback();
    // }
    // e.printStackTrace();
    // } finally {
    // entityManager.close();
    // }
    // return employeeID;
    // }

    // /* Method to READ all the employees */
    // public void listEmployees() {
    // EntityManager entityManager = entityManagerFactory.createEntityManager();
    // EntityTransaction entityTransaction = null;
    // try {
    // entityTransaction = entityManager.getTransaction();
    // entityTransaction.begin();
    // List<Employee> employees = entityManager.createQuery("from
    // Employee").getResultList();
    // for (Employee employee : employees) {
    // System.out.print("ID: " + employee.getId());
    // System.out.print(" Name: " + employee.getName());
    // System.out.println(" Salary: " + employee.getSalary());
    // }
    // entityTransaction.commit();
    // } catch (Exception e) {
    // if (entityTransaction != null) {
    // entityTransaction.rollback();
    // }
    // e.printStackTrace();
    // } finally {
    // entityManager.close();
    // }
    // }

    // /* Method to UPDATE salary for an employee */
    // public void updateEmployee(int EmployeeID, int salary) {
    // EntityManager entityManager = entityManagerFactory.createEntityManager();
    // EntityTransaction entityTransaction = null;
    // try {
    // entityTransaction = entityManager.getTransaction();
    // entityTransaction.begin();
    // Employee employee = entityManager.find(Employee.class, EmployeeID);
    // employee.setSalary(salary);
    // entityManager.persist(employee);
    // entityTransaction.commit();
    // } catch (Exception e) {
    // if (entityTransaction != null) {
    // entityTransaction.rollback();
    // }
    // e.printStackTrace();
    // } finally {
    // entityManager.close();
    // }
    // }

    // /* Method to DELETE an employee from the records */
    // public void deleteEmployee(int EmployeeID) {
    // EntityManager entityManager = entityManagerFactory.createEntityManager();
    // EntityTransaction entityTransaction = null;
    // try {
    // entityTransaction = entityManager.getTransaction();
    // entityTransaction.begin();
    // Employee employee = entityManager.find(Employee.class, EmployeeID);
    // entityManager.remove(employee);
    // entityTransaction.commit();
    // } catch (Exception e) {
    // if (entityTransaction != null) {
    // entityTransaction.rollback();
    // }
    // e.printStackTrace();
    // } finally {
    // entityManager.close();
    // }
    // }

}