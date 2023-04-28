package com.ignotus.GUI;

import com.ignotus.chats.Client;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControllerClient {
    @FXML
    VBox vbox;
    public Client client;
    static Map<String, Client> chatArray = new HashMap<>();

    public void loadChats(ArrayList<ControllerClient> clientArray) {

        for (ControllerClient clientCtrl: clientArray){
            chatArray.put(clientCtrl.client.username, clientCtrl.client);
            addChat(client.username);
        }
    }

    public void addChat(String name) {
        HBox hBox = new HBox();
        Circle circle = new Circle();
        Text text = new Text();

        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeight(54);
        hBox.prefWidth(300);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(20,0,0,0));

        circle.setFill(Color.DODGERBLUE);
        circle.setRadius(19);
        circle.setStroke(Color.TRANSPARENT);

        text.setText(name);

        hBox.getChildren().addAll(circle,text);
        vbox.getChildren().add(hBox);

        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
                Scene scene;
                try {
                    scene = new Scene(fxmlLoader.load(), 300, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage stage = new Stage();
                stage.setTitle("");
                stage.setScene(scene);
                stage.show();
            }
        });

    }

}
