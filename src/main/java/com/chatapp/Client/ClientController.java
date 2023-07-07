package com.chatapp.Client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML Button sendButton;
    @FXML TextField textField;
    @FXML VBox vBox;
    @FXML ScrollPane scrollPane;

    Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client();

        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });
        sendButton.setDefaultButton(true);
        client.receiveMessage(vBox);
    }

    public void sendMessage() {
        String message = textField.getText();
        if(message!=null){
            messageBox(message);
            sendInBackground(message);
            textField.clear();
        }
    }

    private void sendInBackground(String message) {
        Task<Void> sendTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                client.sendMessage(message);
                return null;
            }
        };

        sendTask.setOnFailed(e -> {
            Throwable exception = sendTask.getException();
            System.out.println("Error sending message: " + exception.getMessage());
        });

        new Thread(sendTask).start();
    }

    public void messageBox(String message){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5,10,5,10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:#eff2ff;"+"-fx-background-color:#666666;"+"-fx-background-radius:8px;");
        textFlow.setPadding(new Insets(5,5,5,5));
        text.setFill(Color.color(0.934,0.925,0.996));

        hBox.getChildren().add(textFlow);
        vBox.getChildren().add(hBox);
    }
    public static void receiveMessage(String message,VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,10,5,10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:#eff2ff;"+"-fx-background-color:#666666;"+"-fx-background-radius:8px;");
        textFlow.setPadding(new Insets(5,5,5,5));
        text.setFill(Color.color(0.934,0.925,0.996));

        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}
