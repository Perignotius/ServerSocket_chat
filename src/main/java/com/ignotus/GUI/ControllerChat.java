package com.ignotus.GUI;

import com.ignotus.chats.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.*;

public class ControllerChat {
    Client client;
    Scene scene;
    ControllerClient controllerClient;
    @FXML
    TextField messageField;
    @FXML
    VBox messageBox;
    @FXML
    Text chatName;
    Map<String, LinkedList<Label>> chatMap = new HashMap<>();
    String receiver;
    public void loadTexts() {
        messageBox.getChildren().clear();
        messageBox.getChildren().addAll(chatMap.get(receiver));
        chatName.setText(receiver);
    }

    public void setReceiver(String receiver) {
        client.sendMessage("/connect" + " " + receiver);
        this.receiver = receiver;
    }

    public void addReceiver(String receiver){
        chatMap.put(receiver, new LinkedList<Label>());
    }

    public void sendMessage() {
        client.sendMessage(receiver + "~" + client.username + "~" + messageField.getText());

        Label label = new Label(messageField.getText());
        label.setStyle("-fx-background-color: #ADD8E6; -fx-background-radius: 10; -fx-padding: 5px;");
        label.setPadding(new Insets(10,0,0,10));

        chatMap.get(receiver).add(label);
        messageBox.getChildren().add(label);
        messageField.clear();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void startListener() {
        Listener listener = new Listener();
        listener.start();
    }

    public void startGroupListener(Client group) {
        GroupListener listener = new GroupListener(group);
        listener.start();
    }

    public void setControllerClient(ControllerClient controllerClient) {
        this.controllerClient = controllerClient;
    }

    @FXML
    void sceneBack(){
        messageBox.getChildren().clear();
        controllerClient.getStage().setScene(controllerClient.getScene());
    }
    private class Listener extends Thread{
        @Override
        public void run() {
            while (readMessage() != null){}
        }
        public String readMessage() {
            String msg = client.readMessage();
            if (msg == null)
                return null;

            String[] arr;
            arr = msg.split(" ",-1);
            if (Objects.equals(arr[0], "/groupAdded")){
                String name = arr[1];
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controllerClient.addChat(name);
                        controllerClient.groupBox.getChildren().remove(controllerClient.groupBox.getChildren().size()-1);
                    }
                });

            } else {
                //gauna receiver~sender~msg
                arr = msg.split("~",-1);
                Label label = new Label(arr[2]);
                Label name = new Label(arr[0] + ":");

                label.setStyle("-fx-background-color: #DCDCDC; -fx-background-radius: 10; -fx-padding: 5px;");
                label.setPadding(new Insets(10,0,0,10));

                chatMap.get(arr[1]).add(name);
                chatMap.get(arr[1]).add(label);

                if (Objects.equals(arr[1], receiver)){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            messageBox.getChildren().add(name);
                            messageBox.getChildren().add(label);
                        }
                    });
                }

            }

            return msg;
        }
    }

    private class GroupListener extends Thread{
        Client group;
        GroupListener (Client group) {
            this.group = group;
        }
        @Override
        public void run() {
            while (readMessage() != null){}
        }
        public String readMessage() {
            String msg = group.readMessage();
            group.sendMessage(msg);

            return msg;
        }
    }

}
