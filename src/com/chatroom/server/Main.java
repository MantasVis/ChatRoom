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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatroomServer2.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("Instant messenger server");
            Scene scene = new Scene(root, 740, 520);
            scene.getStylesheets().add("/redLounge.css");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Critical error, this shouldn't be happening...");
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

//todo: Colour code text based on who sent it (user, server, notifications, etc.)
//todo: Work on GUI
//todo: Get a custom CSS going
//todo: Allow the server to terminate the connection
//todo: Change commands from strings to enums

//KNOWN BUGS
//todo: Enter key creates a line break instead of being cleared