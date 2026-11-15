package com.github.harryssuperman.fxdatepicker.demo;

import com.github.harryssuperman.fxdatepicker.controls.EnhancedDatePicker;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class DatePickerDemo extends Application {
    @Override
    public void start(Stage stage) {
        Label heading = new Label("Selector de fecha:");
        heading.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1d2b45;");

        VBox root = new VBox(24, heading, new EnhancedDatePicker());
        root.setPadding(new Insets(32));
        stage.setTitle("FX Enhanced Date Picker");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
