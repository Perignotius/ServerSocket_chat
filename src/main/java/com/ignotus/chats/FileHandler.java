package com.ignotus.chats;

import java.io.*;
import java.util.Scanner;

public class FileHandler {
    File file;
    Scanner scanner;
    FileWriter writer;
    public FileHandler (String fileName) {
        file = new File(fileName);
        try {
            scanner = new Scanner(file);
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
