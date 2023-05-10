package com.ignotus.GUI;

import com.ignotus.chats.Server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

// @Justas Balčiūnas

//https://www.baeldung.com/a-guide-to-java-sockets
//https://www.youtube.com/watch?v=_1nqY-DKP9A

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

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                server.stopServer();
                Platform.exit();
            }
        });

    }

    public static void main(String[] args) {
        launch();
    }
}


/*
trūkumai:
 * grupes sukuriamos kuriancio vartotojo puseje, ne serveryje, todel jei tas vartotojas atsijungia, gruopes nebeveikia...
 * nauji vartotojai uzkraunami per ControlerDev, todel pridejus nauja vartotoja kitame kompiuteryje jis neatsiras sarase...
*/