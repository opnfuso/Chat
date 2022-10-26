package Screens;

import Client.*;
import Utilities.Codes;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Chat extends JFrame implements ListSelectionListener {

    private Container contenedor;
    public String name, currentReceptor = null;
    private boolean isReceptorGroup = false;
    private JLabel grupos, amigos, online, offline, titulo;
    private JButton enviar, select;
    private JTextField mensaje;
    private JList listaGrupos, listaAmigos, listaOnline, listaOffline, listaMensajes;
    private DefaultListModel onlineModel, offlineModel, friendModel, groupModel, messageModel;
    private JScrollPane scrollGrupos, scrollAmigos, scrollOnline, scrollOffline, scrollMensajes;
    private Client current;
    private DataInputStream in;
    private DataOutputStream out;
    private String[] mensajesGrupo, mensajesHumano;

    public Chat(String nombre, Client cliente) {

        current = cliente;
        try {
            in = new DataInputStream(current.client.getInputStream());
            out = new DataOutputStream(current.client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

        name = nombre;
        iniciarComponentos();
        setTitle("COCO CHAT" + name);
        setSize(666, 666);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }

    private void iniciarComponentos() {
        this.setLocation(500, 100);
        contenedor = getContentPane();
        contenedor.setLayout(null);
        mensaje = new JTextField();
        mensaje.setBounds(240, 520, 300, 100);
        titulo = new JLabel();
        titulo.setBounds(400, 10, 100, 20);
        titulo.setText("titulo");

        select = new JButton();
        select.setText("S E L E C C I O N A R");
        select.setBounds(10, 400, 200, 50);
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //setMessages();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    current.logOut();
                } catch (IOException ex) {
                    Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        enviar = new JButton();
        enviar.setText("E N V I A R");
        enviar.setBounds(545, 545, 100, 50);
        enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = mensaje.getText();
                String serverMessage = "";
                if (!message.isEmpty()) {
                    if (currentReceptor != null) {
                        try {
                            if (isReceptorGroup) {
                                serverMessage = Codes.SEND_GROUP_MESSAGE;
                            } else {
                                serverMessage = Codes.SEND_MESSAGE;
                            }
                            //currentReceptor = (String) listaGrupos.getSelectedValue();
                            current.sendMessage(name, message, currentReceptor, serverMessage);
                            mensaje.setText("");
                            setMessages();
                        } catch (IOException ex) {
                            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane,
                                "Seleccione un receptor de las listas de la izquierda");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane,
                            "Cuerpo del mensaje vacío");
                }
            }
        });

        online = new JLabel();
        online.setText("O F F L I N E");
        online.setBounds(80, 0, 200, 20);

        offline = new JLabel();
        offline.setText("O N L I N E");
        offline.setBounds(80, 100, 200, 20);

        amigos = new JLabel();
        amigos.setText("A M I G O S");
        amigos.setBounds(80, 200, 200, 20);

        grupos = new JLabel();
        grupos.setText("G R U P O S");
        grupos.setBounds(80, 300, 200, 20);

        listaMensajes = new JList();
        onlineModel = new DefaultListModel();
        offlineModel = new DefaultListModel();
        friendModel = new DefaultListModel();
        groupModel = new DefaultListModel();
        setFields();

    }

    private void setFields() {
        String id = "";
        String[] friendsof_user = new String[0];
        String[] groups = new String[0];
        String[] offline_users = new String[0];
        String[] online_users = new String[0];
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            out.writeUTF("3");
            out.writeUTF(name);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (in.available() > 0) {
                arrayList.add(in.readUTF());
            }
            friendsof_user = arrayList.toArray(new String[0]);
            arrayList = new ArrayList<String>();
            out.writeUTF("0");
            out.writeUTF(name);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (in.available() > 0) {
                arrayList.add(in.readUTF());
            }
            groups = arrayList.toArray(new String[0]);
            arrayList = new ArrayList<String>();
            out.writeUTF("2");
            out.writeUTF(name);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (in.available() > 0) {
                arrayList.add(in.readUTF());
            }
            offline_users = arrayList.toArray(new String[0]);
            arrayList = new ArrayList<String>();
            out.writeUTF("1");
            out.writeUTF(name);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (in.available() > 0) {
                arrayList.add(in.readUTF());
            }
            online_users = arrayList.toArray(new String[0]);
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        //String[] friendsof_user = db.getList(Codes.FRIENDS_LIST, name);
        // String[] groups = db.getList(Codes.GROUP_LIST, name);

        //String[] offline_users = db.getList(Codes.DISCONNECTED_LIST, name);
        //String[] online_users = db.getList(Codes.CONNECTED_LIST, name);
        //Meter esto a un fori para que se puedan mostrar dependiendo de la cantidad de usuarios.
        onlineModel = new DefaultListModel();
        for (int i = 0; i < online_users.length; i++) {

            onlineModel.addElement(online_users[i]);
        }

        offlineModel = new DefaultListModel();
        for (int i = 0; i < offline_users.length; i++) {
            offlineModel.addElement(offline_users[i]);
        }

        friendModel = new DefaultListModel();
        for (int i = 0; i < friendsof_user.length; i++) {
            friendModel.addElement(friendsof_user[i]);
        }

        groupModel = new DefaultListModel();
        for (int i = 0; i < groups.length; i++) {
            groupModel.addElement(groups[i]);
        }

        listaOnline = new JList(online_users);
        listaOnline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOnline.addListSelectionListener(this);
        scrollOnline = new JScrollPane(listaOnline);
        scrollOnline.setBounds(10, 20, 220, 80);

        listaOffline = new JList(offline_users);
        listaOffline.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOffline.addListSelectionListener(this);
        scrollOffline = new JScrollPane(listaOffline);
        scrollOffline.setBounds(10, 120, 220, 80);

        listaAmigos = new JList(friendsof_user);
        listaAmigos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAmigos.addListSelectionListener(this);
        scrollAmigos = new JScrollPane(listaAmigos);
        scrollAmigos.setBounds(10, 220, 220, 80);

        listaGrupos = new JList(groups);
        listaGrupos.getCellBounds(listaGrupos.getMinSelectionIndex(), listaGrupos.getMaxSelectionIndex());
        listaGrupos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaGrupos.addListSelectionListener(this);
        scrollGrupos = new JScrollPane(listaGrupos);
        scrollGrupos.setBounds(10, 320, 220, 80);

        contenedor.add(grupos);
        contenedor.add(amigos);
        contenedor.add(offline);
        contenedor.add(online);
        contenedor.add(enviar);
        contenedor.add(mensaje);
        contenedor.add(offline);
        contenedor.add(scrollOnline);
        contenedor.add(scrollGrupos);
        contenedor.add(scrollOffline);
        contenedor.add(scrollAmigos);
        contenedor.add(select);
        contenedor.add(titulo);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            return;
        }
        if (e.getSource().equals(listaAmigos)) {
            listaGrupos.clearSelection();
            listaOnline.clearSelection();
            listaOffline.clearSelection();
            isReceptorGroup = false;
            currentReceptor = (String) listaAmigos.getSelectedValue();
            setMessages();
        } else if (e.getSource().equals(listaGrupos)) {
            listaOnline.clearSelection();
            listaAmigos.clearSelection();
            listaOffline.clearSelection();
            isReceptorGroup = true;
            currentReceptor = (String) listaGrupos.getSelectedValue();
            setGroupMessages();
        } else if (e.getSource().equals(listaOnline)) {
            listaOffline.clearSelection();
            listaGrupos.clearSelection();
            listaAmigos.clearSelection();
            isReceptorGroup = false;
            currentReceptor = (String) listaOnline.getSelectedValue();
            setMessages();
        } else if (e.getSource().equals(listaOffline)) {
            listaGrupos.clearSelection();
            listaOnline.clearSelection();
            listaAmigos.clearSelection();
            isReceptorGroup = false;
            currentReceptor = (String) listaOffline.getSelectedValue();
            setMessages();
        }

    }

    private void setGroupMessages() {
        String value = (String) listaGrupos.getSelectedValue();
        currentReceptor = value;
        titulo.setText(value);
        String nombre = value;
        String[] mensajes = new String[0];
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            out.writeUTF("" + Codes.GET_GROUP_MESSAGES);
            out.writeUTF("" + nombre);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (in.available() > 0) {
                arrayList.add(in.readUTF());
            }
            mensajes = arrayList.toArray(new String[0]);
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
        messageModel = new DefaultListModel();
        messageModel.removeAllElements();
        for (int i = 0; i < mensajes.length; i++) {
            messageModel.addElement(mensajes[i]);
        }
        listaMensajes = new JList(mensajes);
        scrollMensajes = new JScrollPane(listaMensajes);
        scrollMensajes.setBounds(240, 40, 400, 400);
        contenedor.add(scrollMensajes);

    }

    public void setMessages() {

        String value = currentReceptor;
        titulo.setText(value);
        String[] mensajes = new String[0];
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            out.writeUTF("5");
            out.writeUTF(name);
            out.writeUTF(value);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (in.available() > 0) {
                arrayList.add(in.readUTF());
            }
            mensajes = arrayList.toArray(new String[0]);
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

        messageModel = new DefaultListModel();
        messageModel.removeAllElements();
        for (int i = 0; i < mensajes.length; i++) {
            messageModel.addElement(mensajes[i]);
        }
        listaMensajes = new JList(mensajes);
        scrollMensajes = new JScrollPane(listaMensajes);
        scrollMensajes.setBounds(240, 40, 400, 400);
        contenedor.add(scrollMensajes);

    }
}
