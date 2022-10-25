/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import connector.Client;

/**
 *
 * @author demom
 */
public class ChatServer {

    /**
     * @param args the command line arguments
     */
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws Exception 
    {
        boolean run = true;

        try 
        {
            ServerSocket server;
            Socket session;
            Thread client;

            server = new ServerSocket(SERVER_PORT);

            while (run)
            {
                session = server.accept();
                client = new Client(session);
                client.start();
            }

            server.close();
        } 
        catch (IOException e)
        {

        }
    }
    
}
