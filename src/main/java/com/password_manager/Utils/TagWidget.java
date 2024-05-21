package com.password_manager.Utils;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;

public class TagWidget extends Label {
    private MFXButton closeButton;
    private final BooleanProperty readonly = new SimpleBooleanProperty(false);

    public TagWidget() {
        super();
        initTag();
    }

    public TagWidget(String text) {
        super(text);
        initTag();
    }

    public TagWidget(String text, String color) {
        super(text);
        color = color.substring(2);

        // Set the CSS style for background color
        setStyle("-fx-background-color: #" + color + ";");
        initTag();
    }

    public TagWidget(String text, Node graphic) {
        super(text, graphic);
    }

    private final void initTag() {
        getStyleClass().add("tag");

        closeButton = new MFXButton();
        closeButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        closeButton.setGraphic(new FontIcon(Feather.X));
        closeButton.getStyleClass().add("tag-close-button");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node n = TagWidget.this.getParent();
                if (n instanceof Pane) {
                    ((Pane) n).getChildren().remove(TagWidget.this);
                }
            }
        });

        // setPadding(new Insets(3, 5, 3, 5));
        setGraphic(closeButton);
        setContentDisplay(ContentDisplay.RIGHT);
        setGraphicTextGap(5);

        graphicProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> paramObservableValue,
                    Node paramT1, Node paramT2) {
                if (paramT2 != closeButton) {
                    setGraphic(closeButton);
                }
            }
        });
        // Bind the visibility of the close button to the manage property
        // closeButton.managedProperty().bind(closeButton.visibleProperty());
        // Bind the visibility of the close button to the readonly property
        readonly.addListener((observable, oldValue, newValue) -> closeButton.setVisible(!newValue));
        closeButton.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // If button is being hidden, adjust layout constraints to prevent it from
                // taking up space
                closeButton.setPrefWidth(0); // Set preferred width to 0
                closeButton.setMinWidth(0); // Set minimum width to 0
                closeButton.setMaxWidth(0); // Set maximum width to 0
            } else {
                // If button is being shown, reset layout constraints to their original values
                closeButton.setPrefWidth(Control.USE_COMPUTED_SIZE); // Reset preferred width
                closeButton.setMinWidth(Button.USE_PREF_SIZE); // Reset minimum width
                closeButton.setMaxWidth(Double.MAX_VALUE); // Reset maximum width
            }
        });

    }

    public boolean isReadonly() {
        return readonly.get();
    }

    public void setReadonly(boolean readonly) {
        this.readonly.set(readonly);
    }

    public BooleanProperty readonlyProperty() {
        return readonly;
    }

    public boolean getBtnVisible() {
        return closeButton.isVisible();
    }
}
