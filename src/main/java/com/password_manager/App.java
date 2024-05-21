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
        stage.setTitle("Secure Squirrel");
        // hostServices
        hostServices = getHostServices();
    }

    public static void main(String[] args) {
        CSSFX.start();
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
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
}