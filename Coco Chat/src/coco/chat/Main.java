/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coco.chat;

import Client.Client;
import Screens.Chat;
import Screens.LoginScreen;
import java.awt.Component;
import java.io.IOException;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Ivano
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//
//        Chat chat = new Chat();
//        chat.setVisible(true);
        try {
            System.out.println("Run");
            Client client = new Client("127.0.0.0", 7777);
            client.start();
            LoginScreen chat = new LoginScreen(client);
            chat.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
