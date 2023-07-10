package com.chatapp.Client;

import com.chatapp.DAO.Message;
import com.chatapp.DAO.MessageDAO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML Button sendButton;
    @FXML TextField textField;
    @FXML VBox vBox;
    @FXML ScrollPane scrollPane;

    Client client;
    MessageDAO messageDAO = new MessageDAO();

    String sender = "Client";
    String receiver = "Server";
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

        displaySavedMessage();
    }

    public void sendMessage() {
        String message = textField.getText();
        Message messageObject = createMessageObject(sender,receiver,message);

        if(message!=null){
            sendMessageBox(message);
            sendInBackground(message);
            messageDAO.saveMessage(messageObject);
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

    public void sendMessageBox(String message){
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

    public Message createMessageObject(String sender,String receiver,String content){
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);

        return message;
    }

    public void displaySavedMessage(){
        List<Message> savedMessage = messageDAO.getMessages(sender,receiver);

        for(Message message: savedMessage){
            receiveMessage(message.getContent(),vBox);
            sendMessageBox(message.getContent());
        }
    }
}
