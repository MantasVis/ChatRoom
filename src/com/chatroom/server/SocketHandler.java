package com.chatroom.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class SocketHandler extends Thread
{
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String username;
    private Server server;

    public SocketHandler(Socket socket, Server server)
    {
        this.socket = socket;
        this.server = server;
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

        server.getClients().add(this);
    }

    /**
     * Receives messages
     */
    private void whileChatting() throws IOException
    {
        String message = "";
        ObjectOutputStream tempOutput;
        ArrayList<SocketHandler> clients = server.getClients();
        server.ableToType(true);

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
                    server.showMessage(message + "\n", MessageType.Message);
                }
            }
            catch (ClassNotFoundException e)
            {
                server.showMessage("\nUser didn't send a string", MessageType.Error);
            }
            catch (SocketException e)
            {

            }
        }
        while (!message.contains("LOLOLOLOASDAD"));

        for (int i = 0; i < clients.size(); i++)
        {
            tempOutput = clients.get(i).getOutput();
            tempOutput.writeObject(username + " has disconnected");
            tempOutput.flush();
        }
        server.updateUsers();
    }

    private void processCommand(String command) throws IOException
    {
        ArrayList<SocketHandler> clients = server.getClients();
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
            server.updateUsers();
            server.showMessage(username + " has connected\n", MessageType.Connection);
        }
        else if (command.contains("END_CONNECTION"))
        {
            for (int i = 0; i < clients.size(); i++)
            {
                tempOutput = clients.get(i).getOutput();
                tempOutput.writeObject(username + " has disconnected");
                tempOutput.flush();
            }
            server.showMessage(username + " has disconnected\n", MessageType.Connection);
            clients.remove(this);
            closeConnection();
            server.updateUsers();

            if (clients.size() == 0)
            {
                server.ableToType(false);
            }
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
}
