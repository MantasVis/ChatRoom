package com.chatroom.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/fxml/chatroomServer.fxml"));
        primaryStage.setTitle("Instant messenger");
        primaryStage.setScene(new Scene(root, 723, 520));
        primaryStage.show();
    }

    @Override
    public void stop()
    {
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}

//todo: Add validation to username input
//todo: Colour code text based on who sent it (user, server, notifications, etc.)
//todo: Make socket ip configurable when launching the app