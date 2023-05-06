package com.ignotus.GUI;

import com.ignotus.chats.Client;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ControllerClient {
    @FXML
    VBox vbox;
    @FXML
    VBox groupBox;
    @FXML
    TextField groupField;
    private Stage stage;
    private Scene scene;
    public Client client;
    public ControllerChat controllerChat;


    public void loadChats(ArrayList<ControllerClient> clientArray) throws InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        Scene newscene;
        try {
            newscene = new Scene(fxmlLoader.load(),300,500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controllerChat = fxmlLoader.getController();
        controllerChat.setClient(client);
        controllerChat.setScene(newscene);
        controllerChat.client.sendMessage("/user");
        Thread.sleep(500);
        controllerChat.client.sendMessage("/name " +  client.username);
        Thread.sleep(500);
        controllerChat.startListener();

        for (ControllerClient clientCtrl: clientArray){
            addChat(clientCtrl.client.username);
        }
    }

    public void addChat(String outputClient) {

        HBox hBox = new HBox();
        Circle circle = new Circle();
        Text text = new Text();

        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.prefHeight(54);
        hBox.prefWidth(300);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(20,0,0,20));

        circle.setFill(Color.DODGERBLUE);
        circle.setRadius(19);
        circle.setStroke(Color.TRANSPARENT);

        text.setText(outputClient);

        hBox.getChildren().addAll(circle,text);
        vbox.getChildren().add(hBox);

        groupBox.getChildren().add(new CheckBox(outputClient));

        controllerChat.setReceiver(outputClient);
        controllerChat.addReceiver(outputClient);

        hBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                controllerChat.setReceiver(outputClient);
                controllerChat.loadTexts();

                stage.setScene(controllerChat.getScene());
                stage.show();
            }
        });
    }

    public void createGroup () throws IOException {

        Client groupClient = new Client("localhost",4200, groupField.getText());
        groupClient.sendMessage("/group");
        groupClient.sendMessage("/name " +  groupField.getText());

        for (Node node: groupBox.getChildren()){
            CheckBox box = (CheckBox)node;
            System.out.println(box.getText());
            if((box).isSelected())
                groupClient.sendMessage("/connect " + box.getText() + " " + groupField.getText());
        }
        groupClient.sendMessage("/connect " + client.username + " " + groupField.getText());
        controllerChat.startGroupListener(groupClient);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }
}
