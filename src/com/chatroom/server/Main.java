package com.chatroom.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            System.err.println("FXML RESOURCE: " + getClass().getResource("/chatroomServer.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatroomServer.fxml"));
            //loader.setController(new ControllerMain());
            Parent root = loader.load();
            primaryStage.setTitle("Instant messenger server");
            primaryStage.setScene(new Scene(root, 723, 520));
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Somethings fucking up...");
        }
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