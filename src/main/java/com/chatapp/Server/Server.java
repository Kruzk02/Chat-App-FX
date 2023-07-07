package com.chatapp.Server;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.clientSocket = serverSocket.accept();
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(clientSocket.getOutputStream(),true);
            logger.info("SERVER IS RUNNING.....");
        } catch (Exception e) {
            System.out.println("ERROR:"+e.getMessage());
            close();
        }
    }

    public void sendMessage(String message){
        try{
            writer.println(message);
            System.out.println("Sent to client: "+message);

            String response = reader.readLine();
            System.out.println(response);
        } catch (IOException e) {
            System.out.println("ERROR:"+e.getMessage());
            close();
        }
    }
    public void receiveMessage(VBox vBox){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (clientSocket.isConnected()){
                    try {
                        String messageFromOther = reader.readLine();
                        ServerController.receiveMessage(messageFromOther,vBox);
                    } catch (Exception e) {
                        System.out.println("RM:"+e.getMessage());
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
            clientSocket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
