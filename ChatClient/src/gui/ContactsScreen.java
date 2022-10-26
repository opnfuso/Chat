package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;

import data.Account;

public class ContactsScreen extends JFrame
{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private JScrollPane scrollPane;
    private JList<String> userListBox;
    DefaultListModel model;
    private int status;
    private ArrayList<String> onlineUsers;

    private ChatScreen chatScreen;

    public ContactsScreen(ChatScreen chatScreen)
    {
        super("Users");
        status = 0;
        onlineUsers = new ArrayList<>();
        model = new DefaultListModel();
        this.chatScreen = chatScreen;
        init();
    }

    private void init()
    {
        // General screen options
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        for (int i = 0; i < onlineUsers.size(); i++)
        {
            model.addElement(onlineUsers.get(i));
        }
        userListBox = new JList(model);
        userListBox.setBackground(Color.DARK_GRAY);
        userListBox.setForeground(Color.LIGHT_GRAY);


        userListBox.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!userListBox.getValueIsAdjusting()) 
                {
                    if (userListBox.getSelectedValue() != null)
                    {
                        chatScreen.setTitle("Chat: " + Account.get().getName() + " - " + userListBox.getSelectedValue());
                    }
                    else
                    {
                        chatScreen.setTitle("Chat: " + Account.get().getName());
                    }
                    
                    chatScreen.updateScreen(userListBox.getSelectedValue());
                }
            }
        });

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(userListBox);
        userListBox.setLayoutOrientation(JList.VERTICAL);
        add(scrollPane);
    }

    public int getStatus() 
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setOnlineUsers(ArrayList<String> onlineUsers) 
    {
        model.clear();

        ArrayList<String> onUsers = new ArrayList<String>(); 
        for (int i = 0; i < onlineUsers.size(); i++)
        {
            if (!onlineUsers.get(i).equals(Account.get().getName()))
            {
                model.addElement(onlineUsers.get(i));
                onUsers.add(onlineUsers.get(i));
            }
        }
        this.onlineUsers = onUsers;
    }

    public ArrayList<String> getOnlineUsers() 
    {
        return onlineUsers;
    }

    public void selectChat(String contact)
    {
        userListBox.setSelectedValue(contact, true);
    }
}