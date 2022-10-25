package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.Font;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.json.JSONObject;

import data.Account;
import data.Chat;
import data.Chats;
import data.Message;

public class ChatScreen extends JFrame implements ActionListener {
    private int status;
    private Socket socket;

    JButton sendMessage;
    JTextField messageBox;
    JTextArea chatBox;
    
    private String contact;

    public ChatScreen(Socket socket) 
    {
        super("Chat");
        contact = null;
        status = 0;
        init(socket);
    }

    private void init(Socket socket) 
    {    
        setResizable(false);
        setLocationRelativeTo(null);
        Font font = new Font("Segoe Script", Font.ITALIC, 15);
        JPanel southPanel = new JPanel();

        this.getContentPane().add(BorderLayout.SOUTH, southPanel);
        southPanel.setLayout(new GridBagLayout());
        
        messageBox = new JTextField(33);
        messageBox.setBackground(Color.DARK_GRAY);
        messageBox.setForeground(Color.LIGHT_GRAY);

        sendMessage = new JButton("Send Message");
        sendMessage.setFocusPainted(false);
        sendMessage.setContentAreaFilled(false);
        sendMessage.setOpaque(true);
        sendMessage.setBorder(new LineBorder(Color.LIGHT_GRAY));
        sendMessage.setBackground(new Color(38, 38, 38));
        sendMessage.setForeground(Color.LIGHT_GRAY);
        sendMessage.setEnabled(false);

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setBackground(new Color(38, 38, 38));
        chatBox.setForeground(Color.LIGHT_GRAY);
        
        chatBox.setFont(font);
        this.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        chatBox.setLineWrap(true);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.WEST;
        GridBagConstraints right = new GridBagConstraints();
        right.anchor = GridBagConstraints.EAST;
        right.weightx = 1.0;
        
        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        sendMessage.addActionListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(470, 300);

        this.socket = socket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void actionPerformed(ActionEvent event) 
    {
        if (messageBox.getText().length() < 1) {

        } else {
            Message message = new Message();
            message.setSender(Account.get().getName());
            message.setContent(messageBox.getText());
            message.setReceiver(contact);
            
            Chat chat = Chats.get().getChat(contact);
            chat.getMessages().add(message);

            String messageString = message.getSender() + ": " + message.getContent() + "\n";
            chatBox.append(messageString);
            
            Map<String, String> map = new HashMap<>();
            map.put("op", "3");
            map.put("sender", Account.get().getName());
            map.put("receiver", contact);
            map.put("content", message.getContent());

            JSONObject json = new JSONObject(map);
            String jsonString = json.toString();
            try 
            {
                socket.getOutputStream().write(jsonString.getBytes());
            } catch (IOException e) 
            {
            }

            messageBox.setText("");
        }
    }

    public String getContact() 
    {
        return contact;
    }

    public void setContact(String contact) 
    {
        this.contact = contact;
    }

    public void updateScreen(String contact)
    {
        this.contact = contact; 
        chatBox.setText("");

        if (contact != null)
        {
            sendMessage.setEnabled(true);
            Chat chat = Chats.get().getChat(contact);
            for (int i = 0; i < chat.getMessages().size(); i++)
            {
                Message message = chat.getMessages().get(i);
                String messageString = message.getSender() + ": " + message.getContent() + "\n";
                chatBox.append(messageString);
            }
        }
        else 
        {
            sendMessage.setEnabled(false);
        }
    }
}
