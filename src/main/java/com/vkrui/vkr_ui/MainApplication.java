package com.vkrui.vkr_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 1000);
        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
        FileChooser fileChooser=new FileChooser();

    }

    public static void main(String[] args) {
        launch();
    }
}