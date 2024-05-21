package com.password_manager.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Utils {
    public static void showErrorAlert(String headerText, String bodyText, Stage owner) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(bodyText);
        alert.initOwner(owner);
        alert.showAndWait();
    }
}
