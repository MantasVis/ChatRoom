package com.chatroom.server;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerMain implements Initializable {

    //FXML Objects
    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextFlow onlineUserArea, chatTextArea;
    @FXML
    private ScrollPane chatScrollPane;

    //FXML Actions
    @FXML
    void sendButton(ActionEvent event)
    {
        String message = inputTextArea.getText();
        sendMessage(message);
    }

    @FXML
    void enterKey(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            event.consume();
            String message = inputTextArea.getText();
            sendMessage(message);
        }
    }

    Server server;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {


        chatTextArea.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
            chatTextArea.layout();
            chatScrollPane.layout();
            chatScrollPane.setVvalue(1.0f);
        }));

        inputTextArea.setWrapText(true);
        server = new Server(onlineUserArea, chatTextArea, inputTextArea);
        server.ableToType(false);

        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> server.start());
    }

    public void sendMessage(String message)
    {
        if (!message.equals(""))
        {
            server.sendMessage(message);
        }
        inputTextArea.clear();
    }

}
