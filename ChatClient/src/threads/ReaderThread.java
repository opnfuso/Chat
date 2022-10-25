package threads;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static javax.swing.JOptionPane.showMessageDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Account;
import data.Chat;
import data.Chats;
import data.Message;
import gui.ChatScreen;
import gui.ContactsScreen;
import gui.LoginScreen;

public class ReaderThread extends Thread {
    private Socket socket;
    private LoginScreen loginScreen;
    private ContactsScreen contactsScreen;
    private ChatScreen chatScreen;

    public ReaderThread(Socket socket, LoginScreen loginScreen, ChatScreen chatScreen, ContactsScreen contactsScreen) {
        this.socket = socket;
        this.loginScreen = loginScreen;
        this.chatScreen = chatScreen;
        this.contactsScreen = contactsScreen;
    }

    @Override
    public void run() {
        byte[] data = new byte[2048];

        while (true) {
            try {
                socket.getInputStream().read(data);
                String input = new String(data);
                int code = getCode(input);

                System.out.println(input);

                switch (code) {
                    // Exception error
                    case 1: {
                        String error = getError(input);
                        showMessageDialog(null, error);
                        break;
                    }

                    // Auth success
                    case 2: {
                        getUser(input);
                        showMessageDialog(null, "Welcome to ChatMe, " + Account.get().getName());
                        loginScreen.setStatus(1);
                        break;
                    }

                    // Another instance running session
                    case 3: {
                        showMessageDialog(null, "Another app has just logged in with this account");
                        chatScreen.setStatus(1);
                        break;
                    }

                    // Update contact list
                    case 4: {
                        ArrayList<String> users = getUsers(input);
                        String contact = chatScreen.getContact();
                        createChats(users);
                        contactsScreen.setOnlineUsers(users);
                        if (contactsScreen.getOnlineUsers().contains(contact)) {
                            contactsScreen.selectChat(contact);
                            // chatScreen.updateScreen(contact);
                            // chatScreen.setTitle("Chat: " + Account.get().getName() + " - " + contact);
                        }
                        break;
                    }

                    // Receive message
                    case 5: {
                        Message message = getMessage(input);
                        Chat chat = Chats.get().getChat(message.getSender());
                        // Add message to local chat storage
                        chat.getMessages().add(message);
                        // Chat is actually active
                        if (chatScreen.getContact() != null && chatScreen.getContact().equals(message.getSender())) {
                            chatScreen.updateScreen(message.getSender());
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                showMessageDialog(null, e.getMessage());
            } catch (JSONException e) {
                showMessageDialog(null, e.getMessage());
            }
        }
    }

    private int getCode(String input) throws JSONException {
        JSONObject json = new JSONObject(input);
        return Integer.parseInt(json.getString("code"));
    }

    private String getError(String input) throws JSONException {
        JSONObject json = new JSONObject(input);
        return json.getString("error");
    }

    private void getUser(String input) throws JSONException {
        JSONObject json = new JSONObject(input);

        int id = Integer.parseInt(json.getString("id"));
        String name = json.getString("name");
        String pass = json.getString("pass");

        Account.get().setId(id);
        Account.get().setName(name);
        Account.get().setPass(pass);
    }

    private ArrayList<String> getUsers(String input) throws JSONException {
        ArrayList<String> users = new ArrayList<>();

        JSONObject object = new JSONObject(input);
        String arrayString = object.getString("array");
        JSONArray array = new JSONArray(arrayString);
        for (int i = 0; i < array.length(); i++) {
            JSONObject userJson = array.getJSONObject(i);
            users.add(userJson.getString("name"));
        }

        return users;
    }

    private Message getMessage(String input) throws JSONException {
        JSONObject json = new JSONObject(input);
        String content = json.getString("content");
        String sender = json.getString("sender");
        String receiver = json.getString("receiver");
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);
        return message;
    }

    private void createChat(String contact) {
        Chat chat = new Chat();
        chat.setContact(contact);
        Chats.get().getChats().add(chat);
    }

    private void createChats(ArrayList<String> users) {
        for (int i = 0; i < users.size(); i++) {
            if (!Chats.get().chatExists(users.get(i))) {
                createChat(users.get(i));
            }
        }
    }
}