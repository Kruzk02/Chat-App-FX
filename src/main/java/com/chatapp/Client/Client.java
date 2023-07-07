package com.chatapp.Client;

import javafx.scene.layout.VBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(){
        try{
            socket = new Socket("localhost",8080);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
        } catch (Exception e) {
            logger.error("ERROR: "+e.getMessage());
        }
    }
    public void sendMessage(String message){
        try{
            writer.println(message);
            System.out.println("Sent to server: "+message);

            String response = reader.readLine();
            System.out.println("Receive message from Server:"+response);
        } catch (IOException e) {
            logger.error("ERROR SendMessage: "+e.getMessage());
        }
    }
    public void receiveMessage(VBox vBox){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String messageFromOther = reader.readLine();
                        ClientController.receiveMessage(messageFromOther,vBox);
                    } catch (Exception e) {
                        logger.error("ERROR AT RM: "+e.getMessage());
                        close();
                        break;
                    }
                }
            }
        });
        thread.start();
    }
    public void close(){
        try{
            socket.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}