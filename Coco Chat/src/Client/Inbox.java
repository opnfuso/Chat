/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Screens.Chat;
import Utilities.Codes;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emilio
 */
public class Inbox extends Thread{
    Socket client;
    Chat chatInstance;
    DataOutputStream write;
    DataInputStream read;
    
    public Inbox (Socket client) throws IOException{
        this.client = client;
        this.read = new DataInputStream(client.getInputStream());
        this.write = new DataOutputStream(client.getOutputStream());
    }
    
    public Inbox (Socket client, Chat chatInstance) throws IOException{
        this.client = client;
        this.read = new DataInputStream(client.getInputStream());
        this.write = new DataOutputStream(client.getOutputStream());
        this.chatInstance = chatInstance;
    }
        
    @Override
    public void run() {
        while (true){
            try {
                String code = read.readUTF();
                if (code.equals(Codes.RECEIVE_MESSAGE)){
                    String emisor = read.readUTF();
                    String message = read.readUTF();
                    chatInstance.setMessages();
                    System.out.println("Tienes un mensaje de " + emisor + ":");
                    System.out.println(message);
                }
                else if (code.equals(Codes.LOGOUT)){
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(Inbox.class.getName()).log(Level.SEVERE, null, ex);
                //break;
            }
        }
        System.out.println("Cerrando inbox");     
    }
    
}
