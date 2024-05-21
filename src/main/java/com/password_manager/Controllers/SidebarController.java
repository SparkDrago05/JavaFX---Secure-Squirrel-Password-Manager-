package com.password_manager.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

import javafx.event.ActionEvent;

public class SidebarController {
    @FXML
    private Label logoLabel;
    private MainView mainViewController;

    public void setMainViewController(MainView mainView) {
        this.mainViewController = mainView;
    }

    @FXML
    void showDashboard(ActionEvent event) throws IOException {
        mainViewController.switchView("dashboard");

    }

    @FXML
    void showLogins(ActionEvent event) throws IOException {
        mainViewController.setModelName("login_info");
        mainViewController.switchView("kanban");
    }

    @FXML
    void showPayments(ActionEvent event) throws IOException {
        mainViewController.setModelName("credit_card_info");
        mainViewController.switchView("kanban");
    }

    @FXML
    void showTrash(ActionEvent event) throws IOException {
        mainViewController.setModelName("trash");
        mainViewController.switchView("kanban");
    }

    Label getLogoLabel() {
        return logoLabel;
    }
}
