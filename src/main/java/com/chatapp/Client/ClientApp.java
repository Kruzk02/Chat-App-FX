package com.chatapp.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URL fxml = new File("src/main/resources/com/chatapp/Client.fxml").toURI().toURL();
        URL css = new File("src/main/resources/com/chatapp/style.css").toURI().toURL();

        Parent root = FXMLLoader.load(fxml);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(String.valueOf(css));

        stage.setTitle("Client Chat");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
