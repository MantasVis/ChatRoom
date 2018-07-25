package com.chatroom.chat;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.xml.soap.Text;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.ArrayList;



public class ControllerMain
{
    String username;
    Socket sock;
    BufferedReader reader;
    PrintWriter writer;
    Boolean isConnected = false;

    //FXML Objects
    @FXML
    private JFXTextField usernameField;
    @FXML
    private TextArea onlineUserArea;

    @FXML
    private TextArea chatTextArea;

    //FXML Actions
    @FXML
    void connectButton(ActionEvent event)
    {
        if (!isConnected)
        {
            username = usernameField.getText();
            usernameField.setEditable(false);

            try
            {
                sock = new Socket("127.0.0.1", 5000);
                InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(streamReader);
                writer = new PrintWriter(sock.getOutputStream());
                writer.println(username + " Œhas connected.ŒConnect");
                writer.flush();
                isConnected = true;
            }
            catch (Exception ex)
            {
                chatTextArea.appendText("Cannot connect! Try again. \n");
                usernameField.setEditable(true);
            }
            ListenThread();
        }
        else if (isConnected)
        {
            chatTextArea.appendText("You are already connected. \n");
        }
    }

    public static void writeUsers(ArrayList<User> userList)
    {
        for (int i = 0; i < userList.size(); i++)
        {
            onlineUserArea.appendText();
        }
    }


    public void ListenThread()
    {
        Thread IncomingReader = new Thread (new IncomingReader());
        IncomingReader.start();
    }
}
