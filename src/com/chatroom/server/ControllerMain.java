package com.chatroom.server;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerMain implements Initializable {

    //FXML Objects
    @FXML
    private JFXTextField usernameField;
    @FXML
    private TextArea onlineUserArea, chatTextArea, inputTextArea;

    Server server;

    //FXML Actions
    @FXML
    void connectButton(ActionEvent event) {

    }

    @FXML
    void disconnectButton(ActionEvent event) {

    }

    @FXML
    void sendButton(ActionEvent event)
    {
        String message = inputTextArea.getText();
        if (!message.equals(""))
        {
            server.sendMessage(message);
        }
        inputTextArea.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        server = new Server(chatTextArea, inputTextArea);
        server.ableToType(false);


        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> server.start());
    }


}
