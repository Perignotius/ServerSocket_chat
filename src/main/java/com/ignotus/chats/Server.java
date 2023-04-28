package com.ignotus.chats;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;

public class Server extends Thread{
    final static int port = 4200;
    private ServerSocket serverSocket;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ServerClientThread(clientSocket).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopServer() throws IOException {
        serverSocket.close();
    }

    private static class ServerClientThread extends Thread{
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private PrintWriter writer;
        private BufferedReader reader;
        ServerClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
            try {
                writer = new PrintWriter(clientSocket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String input;
                while ((input = reader.readLine()) != null) {
                    writer.println(input);
                }

                reader.close();
                writer.close();
                clientSocket.close();
            }
            catch (IOException e){
                System.out.println(e);
            }

        }
    }
}

