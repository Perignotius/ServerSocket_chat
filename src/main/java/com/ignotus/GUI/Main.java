package com.ignotus.GUI;

import com.ignotus.chats.Client;
import com.ignotus.chats.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("dev-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 250, 150);
        stage.setTitle("dev");
        stage.setScene(scene);
        stage.show();

        Server server = new Server();
        server.start();

    }

    public static void main(String[] args) {
        launch();
    }
}