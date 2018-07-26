package com.chatroom.server;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ControllerMain {


    //FXML Objects
    @FXML
    private JFXTextField usernameField;
    @FXML
    private TextArea onlineUserArea, chatTextArea, inputTextArea;

    //FXML Actions
    @FXML
    void connectButton(ActionEvent event) {

    }

    @FXML
    void disconnectButton(ActionEvent event) {

    }

    @FXML
    void sendButton(ActionEvent event) {

    }
}
