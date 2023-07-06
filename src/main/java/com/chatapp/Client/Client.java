package com.chatapp.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client(){
        try{
            socket = new Socket("localhost",8080);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
        } catch (Exception e) {
            System.out.println("ERROR:"+e.getMessage());
        }
    }
    public void sendMessage(String message){
        try{
            writer.println(message);
            System.out.println("Sent to server: "+message);

            String response = reader.readLine();
            System.out.println("Receive message from Server:"+response);
        } catch (IOException e) {
            System.out.println("ERROR:"+e.getMessage());
        }
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