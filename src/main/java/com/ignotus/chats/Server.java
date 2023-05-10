package com.ignotus.chats;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server extends Thread{
    final static int port = 4200;
    private Map<String, Socket> socketMap = new HashMap<>();
    private ServerSocket serverSocket;
    private FileHandler file;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            file = new FileHandler("chatLogs.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while (!serverSocket.isClosed()) {
            try {
                Socket newClientSocket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(newClientSocket.getInputStream()));

                Thread thread;
                if (Objects.equals(reader.readLine(), "/group")){
                    thread = new Thread(new ServerGroupThread(newClientSocket));
                } else {
                    thread = new Thread(new ServerClientThread(newClientSocket));
                }
                thread.start();

            } catch (java.net.SocketException e){
                if (Objects.equals(e.getMessage(), "Socket closed")) {
                    System.out.println("Server terminated");
                }
                else {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
            for (Map.Entry<String, Socket> entry: socketMap.entrySet()){
                entry.getValue().close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private class ServerClientThread implements Runnable{
        private Socket clientSocket;
        private PrintWriter writer;
        private BufferedReader reader;
        ServerClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }
        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String input;
                String[] arr;
                while (true) {
                    input = reader.readLine();
                    arr = input.split(" ",-1);
                    // nustato varda dabartinio socket
                    if (Objects.equals(arr[0], "/name")) {
                        socketMap.put(arr[1], clientSocket);
                        file.addChat(arr[1]);
                    }
                    // prisijungia prie output socket
                    else if (Objects.equals(arr[0], "/connect")){
                        writer = new PrintWriter(socketMap.get(arr[1]).getOutputStream(), true);
                    }
                    else if (Objects.equals(arr[0], "/disconnect") || serverSocket.isClosed()) {
                        reader.close();
                        writer.close();
                        clientSocket.close();
                        return;
                    }
                    else if (writer != null){
                        writer.println(input);
                        arr = input.split("~",-1);
                        file.addMessage(arr[1], arr[0] + "~" +  arr[2]);
                    }
                }

            }
            catch (java.net.SocketException e){
                if (Objects.equals(e.getMessage(), "Socket closed")) {
                    System.out.println("Client terminated");
                }
                else {
                    throw new RuntimeException(e);
                }
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }

        }

        public void stop () {
            try {
                reader.close();
                writer.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private class ServerGroupThread implements Runnable{
        private Socket groupSocket;
        private HashMap<String, PrintWriter> writerList;
        private BufferedReader reader;
        public ServerGroupThread (Socket groupSocket) {this.groupSocket = groupSocket;}
        @Override
        public void run() {
            try {
                writerList = new HashMap<>();
                reader = new BufferedReader(new InputStreamReader(groupSocket.getInputStream()));

                String input;
                String[] arr;
                while (true) {
                    //i grupe zinutes siust group~name~message pavidalu
                    input = reader.readLine();
                    arr = input.split(" ",-1);
                    // nustato varda dabartinio socket
                    if (Objects.equals(arr[0], "/name")) {
                        socketMap.put(arr[1], groupSocket);
                        file.addGroup(arr[1]);
                    }
                    // prisijungia prie output socket
                    else if (Objects.equals(arr[0], "/connect")){
                        PrintWriter writer = new PrintWriter(socketMap.get(arr[1]).getOutputStream(), true);
                        writerList.put(arr[1], writer);
                        writer.println("/groupAdded " + arr[2]);
                    }
                    else {
                        arr = input.split("~",-1);
                        for (Map.Entry<String, PrintWriter> writer: writerList.entrySet()){
                            if (!Objects.equals(arr[1], writer.getKey()))
                                writer.getValue().println(arr[1] + "~" + arr[0] + "~" + arr[2]);
                        }
                    }
                }

            }
            catch (java.net.SocketException e){
                if (Objects.equals(e.getMessage(), "Socket closed")) {
                    System.out.println("Group terminated");
                }
                else {
                    throw new RuntimeException(e);
                }
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

}

