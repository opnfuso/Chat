/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatclient;
import java.io.IOException;
import java.net.Socket;

import data.Account;
import gui.LoginScreen;
import gui.ChatScreen;
import gui.ContactsScreen;
import threads.ReaderThread;

/**
 *
 * @author demom
 */
public class ChatClient {

    /**
     * @param args the command line arguments
     */
    private static final String SERVER_IP = "192.168.84.38";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws Exception {
        boolean run = true;

        try {
            // Creates the socket and screens
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            LoginScreen loginScreen = new LoginScreen(socket);
            ChatScreen chatScreen = new ChatScreen(socket);
            ContactsScreen contactsScreen = new ContactsScreen(chatScreen);

            // Create reader thread
            ReaderThread reader = new ReaderThread(socket, loginScreen, chatScreen, contactsScreen);
            reader.start();

            while (run) {
                // Launches login screen and wait for result (change status)
                loginScreen.setStatus(0);
                chatScreen.setStatus(0);

                loginScreen.setVisible(true);
                contactsScreen.setVisible(false);
                chatScreen.setVisible(false);

                while (loginScreen.getStatus() != 1) {
                    Thread.sleep(500);
                }

                contactsScreen.setTitle(Account.get().getName() + ": Contacts");
                chatScreen.setTitle(Account.get().getName() + ": Chat");

                loginScreen.setVisible(false);
                contactsScreen.setVisible(true);
                chatScreen.setVisible(true);

                while (chatScreen.getStatus() != 1) {
                    Thread.sleep(500);
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
