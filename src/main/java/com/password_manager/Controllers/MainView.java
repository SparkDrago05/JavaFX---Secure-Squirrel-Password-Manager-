package com.password_manager.Controllers;

import com.password_manager.App;
import java.io.IOException;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainView {
    @FXML
    private BorderPane mainView;

    @FXML
    private JFXDrawer sidebar;

    @FXML
    private JFXHamburger sidebarToggler;

    @FXML
    private StackPane mainStack;

    private SidebarController sidebarController;
    private KanbanController kanbanController;
    DashboardController dashboardController;
    private Parent dashboardContent;
    private Parent kanbanContent;
    private Parent formContent;
    private String modelName;

    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/sidebar.fxml"));
        Parent sidebarContent = loader.load();
        this.sidebarController = loader.getController();
        this.sidebarController.setMainViewController(this);
        sidebar.setSidePane(sidebarContent);
        sidebar.open();

        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(sidebarToggler);
        transition.setRate(-1);

        // Load the Dashboard view
        FXMLLoader dashboardLoader = new FXMLLoader(App.class.getResource("fxml/dashboard.fxml"));
        this.dashboardContent = dashboardLoader.load();
        this.dashboardController = dashboardLoader.getController();
        // Load the Kanban view
        FXMLLoader kanbanLoader = new FXMLLoader(App.class.getResource("fxml/kanban_view.fxml"));
        this.kanbanContent = kanbanLoader.load();
        this.kanbanController = kanbanLoader.getController();
        this.kanbanController.setMainViewController(this);

        sidebarToggler.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (sidebar.isOpened()) {
                sidebar.close();
            } else {
                sidebar.open();
            }
        });

        sidebar.managedProperty().bind(sidebar.visibleProperty());
        sidebar.setOnDrawerOpening((e) -> {
            sidebar.setVisible(true);
        });

        sidebar.setOnDrawerClosed((e) -> {
            sidebar.setVisible(false);

        });
        // setModelName("login_info");
        // switchView("kanban");
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            App.currentUser = 0;
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/auth2.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 760, 470);
            for (String stylesheetName : App.getStylesheetNames()) {
                scene.getStylesheets().add(App.class.getResource("css/" + stylesheetName).toExternalForm());
            }
            App.getStage().setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setFormContent(Parent formContent) {
        this.formContent = formContent;
    }

    public void switchView(String view) {
        mainStack.getChildren().clear();
        switch (view) {
            case "dashboard":
                dashboardController.updateValues();
                mainStack.getChildren().add(dashboardContent);
                break;
            case "kanban":
                kanbanController.setModelName(this.modelName);
                kanbanController.setRecords();
                kanbanController.layoutCards(kanbanController.getWidth());
                mainStack.getChildren().add(kanbanContent);
                break;
            case "form":
                mainStack.getChildren().add(formContent);
                break;
            default:
                break;
        }
    }

}
