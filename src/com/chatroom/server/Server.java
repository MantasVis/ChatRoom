package com.chatroom.server;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public final class Server
{
    private ServerSocket serverSocket;
    private Socket connection;
    private TextArea inputTextArea;
    private TextFlow onlineUserArea, chatTextArea;
    private ArrayList<SocketHandler> clients = new ArrayList<>();

    public Server(TextFlow onlineUserArea, TextFlow chatTextArea, TextArea inputTextArea)
    {
        this.onlineUserArea = onlineUserArea;
        this.chatTextArea = chatTextArea;
        this.inputTextArea = inputTextArea;
    }

    /**
     * Runs the server
     */
    public void start()
    {
        try
        {
            serverSocket = new ServerSocket(6789, 100);

            while (true)
            {
                try
                {
                    waitForConnection();
                }
                catch (EOFException e)
                {
                    showMessage("\nServer ended the connection!", MessageType.Error);
                }

            }
        }
        catch (IOException e)
        {

        }
    }

    /**
     * Waits for connection and then displays the connection information
     */
    private void waitForConnection() throws IOException
    {
        connection = serverSocket.accept();
        SocketHandler socketHandler = new SocketHandler(connection, this);
        socketHandler.start();

        showMessage("Now connected to " + connection.getInetAddress().getHostName() + "\n", MessageType.Connection);
    }

    /**
     * Sends messages
     */
    public void sendMessage(String message)
    {
        for (int i = 0; i < clients.size(); i++)
        {
            try
            {
                ObjectOutputStream output = clients.get(i).getOutput();
                output.writeObject("SERVER: " + message);
                output.flush();
            }
            catch (IOException e)
            {
                Text t1 = new Text("Error sending message\n");
                chatTextArea.getChildren().add(t1);
            }
        }
        showMessage("SERVER: " + message + "\n", MessageType.Message);
    }

    /**
     * Updates the chat window with text
     */
    public void showMessage(final String text, MessageType messageType)
    {
        Text t1 = new Text(text);

        switch (messageType)
        {
            case Message:
                t1.setFill(Color.WHITE);
                break;

            case Error:
                t1.setFill(Color.RED);
                break;

            case Connection:
                t1.setFill(Color.CYAN);
                break;

            default:
                t1.setFill(Color.YELLOW);
                break;
        }
        Platform.runLater(() -> chatTextArea.getChildren().add(t1));
    }

    /**
     * Updates online user list
     */
    public void updateUsers() throws IOException
    {
        Platform.runLater(() -> onlineUserArea.getChildren().clear());
        ObjectOutputStream tempOutput;
        String users = "";

        for (int i = 0; i < clients.size(); i++)
        {
            Text t1 = new Text(clients.get(i).getUsername() + "\n");
            t1.setFill(Color.WHITE);
            Platform.runLater(() -> onlineUserArea.getChildren().add(t1));
            users = users + clients.get(i).getUsername() + "\n";
        }

        for (int i = 0; i < clients.size(); i++)
        {
            tempOutput = clients.get(i).getOutput();
            tempOutput.writeObject("Œ" + "USER_LIST:" + users);
            tempOutput.flush();
        }
    }

    /**
     * Allows and disallows typing
     */
    public void ableToType(final boolean allowed)
    {
        Platform.runLater(() -> inputTextArea.setEditable(allowed));
    }

    public  ArrayList<SocketHandler> getClients() {
        return clients;
    }
}
