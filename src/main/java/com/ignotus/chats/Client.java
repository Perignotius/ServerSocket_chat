package com.ignotus.chats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public String username;
    private Socket clientSocket;
    private PrintWriter writer;
    private BufferedReader reader;

    public Client (String ip, int port, String username) throws IOException {
        this.username = username;
        clientSocket = new Socket(ip, port);
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    public String readMessage(){
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() throws IOException {
        writer.close();
        reader.close();
        clientSocket.close();
    }
}
