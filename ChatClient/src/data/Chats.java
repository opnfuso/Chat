package data;

import java.util.ArrayList;

public class Chats 
{
    private static Chats instance = null;
    private ArrayList<Chat> chats;

    public Chats()
    {
        chats = new ArrayList<>();
    }

    public static Chats get()
    {
        if (instance == null)
        {
            instance = new Chats();
        }
        return instance;
    }

    public ArrayList<Chat> getChats() 
    {
        return chats;
    }

    public int getCount()
    {
        return chats.size();
    }

    public Chat getChat(String contact)
    {
        for (int i = 0; i < getCount(); i++)
        {
            Chat chat = chats.get(i);
            if (chat.getContact().equals(contact))
            {
                return chat;
            }
        }
        return null;
    }

    public boolean chatExists(String user)
    {
        for (int i = 0; i < getCount(); i++)
        {
            if (chats.get(i).getContact().equals(user))
            {
                return true;
            }
        }
        return false;
    }
}
