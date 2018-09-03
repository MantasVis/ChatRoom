package com.chatroom.server;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static com.chatroom.server.Server.getClients;

public class SocketHandler extends Thread
{
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private TextArea chatTextArea, inputTextArea, onlineUsersArea;
    private String username;

    public SocketHandler(Socket socket, TextArea chatTextArea, TextArea inputTextArea, TextArea onlineUsersArea, String username)
    {
        this.socket = socket;
        this.chatTextArea = chatTextArea;
        this.inputTextArea = inputTextArea;
        this.onlineUsersArea = onlineUsersArea;
        this.username = "";
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
            //closeConnection();
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

        getClients().add(this);
    }

    /**
     * Receives messages
     */
    private void whileChatting() throws IOException
    {
        String message = "";
        ObjectOutputStream tempOutput;
        ArrayList<SocketHandler> clients = getClients();
        ableToType(true);

        do
        {
            try
            {
                message = (String) input.readObject();

                if (message.startsWith("Œ"))
                {
                    processCommand(message);
                }
                else
                {
                    for (int i = 0; i < clients.size(); i++)
                    {
                        tempOutput = clients.get(i).getOutput();
                        tempOutput.writeObject(message);
                        tempOutput.flush();
                    }

                    showMessage("\n" + message);
                }
            }
            catch (ClassNotFoundException e)
            {
                showMessage("\nUser didn't send a string");
            }
        }
        while (!message.contains("LOLOLOLOASDAD"));

        for (int i = 0; i < clients.size(); i++)
        {
            tempOutput = clients.get(i).getOutput();
            tempOutput.writeObject(username + " has disconnected");
            tempOutput.flush();
        }
        updateUsers();
    }

    private void processCommand(String command) throws IOException
    {
        ArrayList<SocketHandler> clients = getClients();
        ObjectOutputStream tempOutput;

        if (command.contains("START_CONNECTION"))
        {
            username = command.split(";")[1];

            for (int i = 0; i < clients.size(); i++)
            {
                tempOutput = clients.get(i).getOutput();
                tempOutput.writeObject(username + " has connected");
                tempOutput.flush();
            }
            updateUsers();
            showMessage(username + " has connected");
        }
        else if (command.contains("END_CONNECTION"))
        {
            for (int i = 0; i < clients.size(); i++)
            {
                tempOutput = clients.get(i).getOutput();
                tempOutput.writeObject(username + " has disconnected\n");
                tempOutput.flush();
            }
            showMessage("\n" + username + " has disconnected");
            clients.remove(this);
            closeConnection();
            updateUsers();
        }
    }

    private void closeConnection()
    {
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

    public String getUsername()
    {
        return username;
    }

    /**
     * Updates the chat window
     */
    private void showMessage(final String text)
    {
        Platform.runLater(() -> chatTextArea.appendText(text));
    }

    /**
     * Updates user list
     */
    private void updateUsers() throws IOException
    {
        Platform.runLater(() -> onlineUsersArea.clear());
        ArrayList<SocketHandler> clients = getClients();
        ObjectOutputStream tempOutput;
        String users = "";

        for (int i = 0; i < getClients().size(); i++)
        {
            int finalI = i;
            Platform.runLater(() -> onlineUsersArea.appendText(getClients().get(finalI).getUsername() + "\n"));
            users = users + getClients().get(finalI).getUsername() + "\n";
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
}
