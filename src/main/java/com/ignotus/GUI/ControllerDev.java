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
    void addUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("selection-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 300, 500);

        for (ControllerClient clientCtrl: clientCtrlArray) {
            System.out.println(clientCtrl.client.username);
            if (Objects.equals(clientCtrl.client.username, userField.getText())) {
                errorField.setText("username in use");
                throw new RuntimeException("username in use");
            }
        }



        ControllerClient clientController = fxmlLoader.getController();
        clientController.loadChats(clientCtrlArray);

        Client newClient = new Client("localhost",4200);
        newClient.username = userField.getText();
        clientCtrlArray.add(clientController);



        Stage stage = new Stage();
        stage.setTitle(userField.getText());
        stage.setScene(scene);
        stage.show();

    }

}