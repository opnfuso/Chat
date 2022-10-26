/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Utilities.Codes;
import Utilities.MySQL;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aroma
 */
public class ServerClient implements Runnable {

    //Propiedades del objeto
    int numero;
    String userName, currentFriend;
    boolean logout, kill = false;
    Socket cliente;
    DataInputStream read;
    DataOutputStream write;
    private String code;

    //Constructor de la clase
    public ServerClient(Socket cliente, int numero) throws IOException {
        this.cliente = cliente;
        this.numero = numero;
        this.userName = "";
        this.logout = false;
        this.read = new DataInputStream(cliente.getInputStream());
        this.write = new DataOutputStream(cliente.getOutputStream());
    }

    public void startClient() {
        while (isDead() == false) {
            try {
                if (read.available() != 0) {
                    System.out.println("No estoy muerto, hay " + read.available() + "bytes disponibles");
                    this.code = read.readUTF();
                    System.out.println("(Manager) Código: " + code);
                    if (code.equals(Codes.REGISTER)) {
                        String email = read.readUTF();
                        String username = read.readUTF();
                        String password = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            db.crearCuenta(username, email, password);
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                    } else if (code.equals(Codes.LOGIN)) {
                        System.out.println("Iniciando sesion");
                        String usersame = read.readUTF();
                        String password = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            if (db.logIn(usersame, password)) {
                                System.out.println("REAL");
                                write.writeUTF("true");
                            } else {
                                System.out.println("FAKE");
                                write.writeUTF("false");
                            }
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                    } else if (code.equals(Codes.SEND_MESSAGE)) {
                        String emisor = read.readUTF();
                        String receptor = read.readUTF();
                        String mensaje = read.readUTF();
                        System.out.println("Emisor: " + emisor);
                        System.out.println("Mensaje: " + mensaje);
                        System.out.println("Recceptor: " + receptor);
                        MySQL db = new MySQL();
                        try {
                            System.out.println("Listo para enviar, server");
                            db.uploadMessage(emisor, receptor, mensaje);
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                    } else if (code.equals(Codes.LOGOUT)) {
                        MySQL db = new MySQL();
                        try {
                            db.switchStatus(userName, false);
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        killThread();
                        break;
                    } else if (code.equals(Codes.SEND_GROUP_MESSAGE)) {
                        String mensaje = read.readUTF();
                        String group = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            System.out.println("Listo para enviar, server");
                            db.uploadGroupMessage(userName, group, mensaje);
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (code.equals("3")) {
                        String name = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            String aux[] = db.getList(Integer.parseInt(code), name);
                            for (String a : aux) {
                                write.writeUTF(a);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (code.equals("0")) {
                        String name = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            String aux[] = db.getList(Integer.parseInt(code), name);
                            for (String a : aux) {
                                write.writeUTF(a);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (code.equals("2")) {
                        String name = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            String aux[] = db.getList(Integer.parseInt(code), name);
                            for (String a : aux) {
                                write.writeUTF(a);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (code.equals("1")) {

                        String name = read.readUTF();
                        System.out.println("Leyendo los usuarios conectados desde el usuario " + name);

                        MySQL db = new MySQL();
                        try {
                            String aux[] = db.getList(Integer.parseInt(code), name);
                            for (String a : aux) {
                                System.out.println(a);
                                write.writeUTF(a);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (code.equals("4")) {
                        String name = read.readUTF();
                        MySQL db = new MySQL();
                        try {
                            String aux[] = db.getGroupMessages(name);
                            for (String a : aux) {
                                write.writeUTF(a);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (code.equals("5")) {
                        
                        String name = read.readUTF();
                        String name1 = read.readUTF();
                        System.out.println("MENSAJES ENTRE " + name + " y " + name1);
                        MySQL db = new MySQL();
                        try {
                            String aux[] = db.getMessages(name, name1);                          
                            for (int i=0; i< aux.length;i++) {
                                write.writeUTF(aux[i]);
                            }
                            for (String a : aux) {
                                System.out.println("mensaje" + a);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        System.out.println(userName + ": desconectad@");
        //printThreads();
    }

    public void notifyMessage(String emisor, String message) throws IOException {
        write.writeUTF(Codes.RECEIVE_MESSAGE);
        write.writeUTF(emisor);
        write.writeUTF(message);
        System.out.println("Tienes un mensaje de " + emisor + ":");
        System.out.println(message);
    }

    public void killThread() throws IOException {
        write.writeUTF(Codes.LOGOUT);
        cliente.close();
        kill = true;
    }

    public boolean isDead() {
        return kill;
    }

    public Socket getSocket() {
        return cliente;
    }

    @Override
    public String toString() {
        return ("Cliente no. " + this.numero);
    }

    @Override
    public void run() {
        startClient();
    }

}
