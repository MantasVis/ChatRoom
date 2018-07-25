package com.chatroom.chat;

import java.util.ArrayList;

import static com.chatroom.chat.ControllerMain.writeUsers;

public class IncomingReader implements Runnable
{
    ArrayList<User> userList = new ArrayList<>();

    @Override
    public void run()
    {
        String stream;
        String[] data;
        String done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

        try
        {
            while ((stream = reader.ReadLine()) != null)
            {
                data = stream.split("Œ");

                if (data[2].equals(chat))
                {
                    chatTextArea.append(data[0] + ": " + data[1] + "\n");
                }
                else if (data[2].equals(connect))
                {
                    chatTextArea.removeAll();
                    userAdd(data[0]);
                }
                else if (data[2].equals(disconnect))
                {
                    userRemove(data[0]);
                }
                else if (data[2].equals(done))
                {
                    writeUsers(userList);
                }
            }
        }
    }

    public void userAdd(String username)
    {
        userList.add(new User(username));
    }

    public void userRemove(String username)
    {
        boolean found = false;

        while (!found)
        {
            for (int i = 0; i < userList.size(); i++)
            {
                if (userList.get(i).getUsername().equals(username))
                {
                    userList.remove(i);
                    found = true;
                }
            }
        }
        chatTextArea.append(username + " has disconnected. \n");
    }

}
