package data;

import java.util.ArrayList;

public class Chat 
{
    private ArrayList<Message> messages;
    private String contact;
    
    public Chat()
    {
        messages = new ArrayList<>();
    }

    public ArrayList<Message> getMessages() 
    {
        return messages;
    }    

    public void setMessages(ArrayList<Message> messages) 
    {
        this.messages = messages;
    }

    public String getContact() 
    {
        return contact;
    }

    public void setContact(String contact) 
    {
        this.contact = contact;
    }
}
