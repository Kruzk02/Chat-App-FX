package com.chatapp.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Server() {
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println("Server started");

            clientSocket = serverSocket.accept();
            System.out.println("Client connected: "+clientSocket);

            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            System.out.println("ERROR:"+e.getMessage());
        }
    }
    public void startListening(){
        try{
            String message;
            while ((message = reader.readLine())!=null){
                System.out.println("Receive from Client:"+message);
                writer.println(message);
            }
            serverSocket.close();
            clientSocket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args){
        Server server = new Server();
        server.startListening();
    }
}
