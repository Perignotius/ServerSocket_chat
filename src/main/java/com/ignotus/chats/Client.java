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

    //https://www.baeldung.com/a-guide-to-java-sockets
    //https://www.youtube.com/watch?v=_1nqY-DKP9A

    public Client (String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    public String sendMessage(String msg) throws IOException {
        writer.println(msg);
        String resp = reader.readLine();
        return resp;
    }

    public void stop() throws IOException {
        writer.close();
        reader.close();
        clientSocket.close();
    }
}
