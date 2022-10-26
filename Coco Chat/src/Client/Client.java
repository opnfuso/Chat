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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author usuario
 */
public class Client extends Thread {

    public Socket client;
    private String dirIP;
    private int port;
    String username;
    DataInputStream read;
    DataOutputStream write;

    public Client(Socket cliente, String username, Chat chat) throws IOException {
        //Instanciar Scanner in para leer datos de prueba        
        //Instanciar datos del usuario        
        this.client = cliente;
        System.out.println("Conectado");
        //Instanciar write y read
        read = new DataInputStream(client.getInputStream());
        write = new DataOutputStream(client.getOutputStream());
        //Enviar userName al server
        this.username = username;
        setName(username);
        Inbox inbox = new Inbox(client, chat);
        inbox.start();
    }

    public Client(String dirIP, int port) throws IOException {
        //Instanciar Scanner in para leer datos de prueba
        //Instanciar los Sockets
        this.dirIP = dirIP;
        this.port = port;
        //Instanciar datos del usuario        
        client = new Socket(dirIP, port);
        System.out.println("Conectado");
        //Instanciar write y read
        read = new DataInputStream(client.getInputStream());
        write = new DataOutputStream(client.getOutputStream());
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String mensaje, currentFriend;
       

    }

    public void sendMessage(String Name, String message, String receptor, String serverMessage) throws IOException {
        System.out.println("Mensaje: " + message);
        System.out.println("Código: " + serverMessage);
        System.out.println("Recceptor: " + receptor);
        write.writeUTF(serverMessage);
        write.writeUTF(Name);
        write.writeUTF(receptor);
        write.writeUTF(message);
        System.out.println("Mensaje enviado\n");
    }

    public void logOut() throws IOException {
        write.writeUTF(Codes.LOGOUT);
    }

    public void enviarRegister(String username, String email, String password) {
        try {
            write.writeUTF(Codes.REGISTER);
            write.writeUTF(username);
            write.writeUTF(email);
            write.writeUTF(password);
        } catch (IOException error) {

        }
    }

    public void enviarLogIn(String username, String password) {
        try {
            write.writeUTF(Codes.LOGIN);
            write.writeUTF(username);
            write.writeUTF(password);
        } catch (IOException error) {

        }
    }

}
