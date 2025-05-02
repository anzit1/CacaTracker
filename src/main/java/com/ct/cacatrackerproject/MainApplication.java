package com.ct.cacatrackerproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("loginpage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        scene.getRoot().requestFocus();
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("CacaTracker Alicante");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}