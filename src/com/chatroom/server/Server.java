package com.chatroom.server;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextArea;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket serverSocket;
    private Socket connection;
    private TextArea chatTextArea, inputTextArea;

    public Server(TextArea chatTextArea, TextArea inputTextArea)
    {
        this.chatTextArea = chatTextArea;
        this.inputTextArea = inputTextArea;
    }

    private void start()
    {
        try
        {
            serverSocket = new ServerSocket(6789, 100);

            while (true)
            {
                try
                {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }
                catch (EOFException e)
                {
                    showMessage("\nServer ended the connection!");
                }
                finally
                {
                    closeServer();
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
        showMessage("Now connected to " + connection.getInetAddress().getHostName());
    }

    /**
     * Get stream to send and retrieve data
     */
    private void setupStreams() throws IOException
    {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());

        showMessage("\nStreams are now set up! \n");
    }

    /**
     * Receives messages
     */
    private void whileChatting() throws IOException
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
        while (!message.equals("CLIENT - END"));
    }

    /**
     * Close streams and sockets before shutting down the server
     */
    private void closeServer()
    {
        showMessage("\nClosing connections... \n");
        ableToType(true);

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
    }

    /**
     * Sends messages
     */
    public void sendMessage(String message)
    {
        try
        {
            output.writeObject("SERVER: " + message);
            output.flush();
            showMessage("\nSERVER: " + message);
        }
        catch (IOException e)
        {
            chatTextArea.appendText("\nError sending message");
        }
    }

    /**
     * Updates the chat window
     */
    private void showMessage(final String text)
    {
        Platform.runLater(() -> chatTextArea.appendText(text));
    }

    public void ableToType(final boolean allowed)
    {
        Platform.runLater(() -> inputTextArea.setEditable(allowed));
    }
}
