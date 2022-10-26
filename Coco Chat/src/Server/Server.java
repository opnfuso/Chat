/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author aroma
 */
public class Server {

    public static void main(String[] args) throws IOException {
        DataInputStream in;
        DataOutputStream out;
        //Instanciar los Sockets
        int numero = 0;
        int PORT = 7777;
        ServerSocket server = new ServerSocket(PORT);
        //Instanciacion de la Array List
        //Bucle que espera a la conexion de los usuario
        System.out.println("Inicia serverz");
        while (true) {
            numero++;
            Socket socket = server.accept();
            System.out.println("Conectado desde " + socket.getInetAddress());
            Thread hilo;
            hilo = new Thread(new ServerClient(socket,numero));
            hilo.start();
        }
    }
}

/*String dirIP;
        dirIP = new String("192.168.15.217");*/
//Instanciando socket cliente
        //Socket client = new Socket();
