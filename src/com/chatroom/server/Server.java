package com.chatroom.server;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class Server
{
    private ServerSocket serverSocket;
    private Socket connection;
    private TextArea chatTextArea, inputTextArea, onlineUserArea;
    private static ArrayList<SocketHandler> clients = new ArrayList<>();

    public Server(TextArea onlineUserArea, TextArea chatTextArea, TextArea inputTextArea)
    {
        this.onlineUserArea = onlineUserArea;
        this.chatTextArea = chatTextArea;
        this.inputTextArea = inputTextArea;
    }

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
                    showMessage("\nServer ended the connection!");
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
        SocketHandler socketHandler = new SocketHandler(connection, chatTextArea, inputTextArea, onlineUserArea, "");
        socketHandler.start();

        showMessage("Now connected to " + connection.getInetAddress().getHostName() + "\n");
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
                chatTextArea.appendText("Error sending message\n");
            }
        }
        showMessage("SERVER: " + message + "\n");
    }

    /**
     * Updates the chat window
     */
    private void showMessage(final String text)
    {
        Platform.runLater(() -> chatTextArea.appendText(text));
    }

    /**
     * Allows and disallows typing
     */
    public void ableToType(final boolean allowed)
    {
        Platform.runLater(() -> inputTextArea.setEditable(allowed));
    }

    public static ArrayList<SocketHandler> getClients() {
        return clients;
    }
}
