package com.example.clientGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;


public class HelloApplication extends Application{
    static String MyName = null;
    public static Socket s = null;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        HelloApplication.stage=stage;
        stage.setTitle("FIRECHAT");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image/title.png")));

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(e->{

        });

    }

    public static void main(String[] args) {
        launch();
    }

}