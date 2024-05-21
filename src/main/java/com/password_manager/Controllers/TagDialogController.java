package com.password_manager.Controllers;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;

public class TagDialogController {

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private MFXTextField tagNameField;

    private FormController formController;

    public void setFormController(FormController formController) {
        this.formController = formController;
    }

    public String getTagName() {
        return tagNameField.getText();
    }

    public String getTagColor() {
        return colorPicker.getValue().toString();
    }

}
