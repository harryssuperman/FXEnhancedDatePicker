package com.github.harryssuperman.fxdatepicker;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static void main( String[] args )
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        DatePicker datepicker = new DatePicker(LocalDate.now());
        StackPane stackPane = new StackPane(datepicker);
        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
