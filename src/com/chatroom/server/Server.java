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
    private TextArea chatTextArea, inputTextArea;
    private static ArrayList<SocketHandler> clients = new ArrayList<>();

    public Server(TextArea chatTextArea, TextArea inputTextArea)
    {
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
        showMessage("Waiting for someone to connect... \n");
        connection = serverSocket.accept();
        SocketHandler socketHandler = new SocketHandler(connection, chatTextArea, inputTextArea);
        socketHandler.start();

        showMessage("Now connected to " + connection.getInetAddress().getHostName());
    }

    /**
     * Set up stream to send and retrieve data
     */
   /* private void setUpStreams() throws IOException
    {

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());

        showMessage("\nStreams are now set up! \n");
    }*/

    /**
     * Receives messages
     */
    /*private void whileChatting() throws IOException
    {
        String message = "You are now connected!";
        sendMessage(message);
        ableToType(true);

        do
        {
            try
            {
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch (ClassNotFoundException e)
            {
                showMessage("\nUser didn't send a string");
            }
        }
        while (!message.equals("USER: END"));
    }*/

    /**
     * Close streams and sockets before shutting down the server
     */
    /*private void closeServer()
    {
        showMessage("\nClosing connections... \n");
        ableToType(false);

        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }*/

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
                chatTextArea.appendText("\nError sending message");
            }
        }
        showMessage("\nSERVER: " + message);
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
