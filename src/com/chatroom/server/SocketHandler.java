package com.chatroom.server;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHandler extends Thread
{
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private TextArea chatTextArea, inputTextArea;

    public SocketHandler(Socket socket, TextArea chatTextArea, TextArea inputTextArea)
    {
        this.socket = socket;
        this.chatTextArea = chatTextArea;
        this.inputTextArea = inputTextArea;
    }

    public void run()
    {
        try
        {
            setUpStreams();
            whileChatting();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }

    }

    /**
     * Set up stream to send and retrieve data
     */
    private void setUpStreams() throws IOException
    {

        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();

        input = new ObjectInputStream(socket.getInputStream());

        Server.getClients().add(this);


        showMessage("\nStreams are now set up! \n");
    }

    /**
     * Receives messages
     */
    private void whileChatting() throws IOException
    {
        String message = "You are now connected!";
        //sendMessage(message);
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
    }
    private void closeConnection()
    {
        showMessage("\nClosing connections... \n");
        ableToType(false);

        try
        {
            output.close();
            input.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutput()
    {
        return output;
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
}
