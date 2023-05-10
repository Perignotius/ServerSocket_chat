package com.ignotus.chats;

import java.io.*;

public class FileHandler {
    private File file;

    public FileHandler (String fileName) throws IOException {
        file = new File((fileName));
    }

    public void addChat(String name) throws IOException {

        FileWriter writer = new FileWriter(file, true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        boolean exists = false;
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("::" + name + "::")) {
                    exists = true;
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!exists)
            writer.write("::" + name + "::\n");
        writer.close();
    }

    public void addGroup (String name) throws IOException {

        FileWriter writer = new FileWriter(file, true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        boolean exists = false;
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("{group}::" + name + "::")) {
                    exists = true;
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!exists)
            writer.write("{group}::" + name + "::\n");
        writer.close();
    }

    public void addMessage(String name, String message) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("::" + name + "::")) {
                    line = line + ". " + message;
                }
                stringBuilder.append(line).append('\n');
            }
            FileWriter writer = new FileWriter(file);
            writer.append(stringBuilder);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        reader.close();
    }


    public String getMessages(String name) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("::" + name + "::")) {
                    reader.close();
                    return line.replace("::"+name+":: ", "");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        reader.close();
        return null;
    }

    public String getLine (int n) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        for (int i=0; i<n ;i++){
            line = reader.readLine();
        }
        reader.close();
        return line;
    }

}
