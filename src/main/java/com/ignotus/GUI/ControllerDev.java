package com.ignotus.GUI;

import com.ignotus.chats.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ControllerDev {

    ArrayList<ControllerClient> clientCtrlArray = new ArrayList<>();
    @FXML
    TextField userField;
    @FXML
    Text errorField;

    @FXML
    void addUser() throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selection-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 500);

        for (ControllerClient clientCtrl: clientCtrlArray) {
            if (Objects.equals(clientCtrl.client.username, userField.getText())) {
                errorField.setText("username in use");
                throw new RuntimeException("username in use");
            }
        }



        ControllerClient clientController = fxmlLoader.getController();

        Client newClient = new Client("localhost",4200, userField.getText());
        clientController.setClient(newClient);
        clientController.loadChats(clientCtrlArray);

        for (ControllerClient ctr: clientCtrlArray){
            ctr.addChat(newClient.username);
        }
        clientCtrlArray.add(clientController);


        Stage stage = new Stage();
        clientController.setStage(stage);
        clientController.setScene(scene);
        clientController.controllerChat.setControllerClient(clientController);
        stage.setTitle(userField.getText());
        stage.setScene(scene);
        stage.show();

    }

}