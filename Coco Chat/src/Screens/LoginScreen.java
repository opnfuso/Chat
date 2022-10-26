/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import Client.Client;
import java.awt.Color;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Ivano
 */
public class LoginScreen extends JFrame {

    private Color mainColor = new Color(250, 242, 255);
    //Elementos del método iniciarSesion:
    private JTextField user;
    private JTextField pswd;
    public String name;
    static String nombre;
    private String contrasena;
    private String contra;

    private JTextField username;
    private JPasswordField password;
    private JButton send;
    private JButton register;

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private Client current;

    public LoginScreen(Client client) {
        current = client;
        chat();

    }

    void chat() {
        setLocation(566, 100);
        setTitle("COCOCHAT");
        setSize(600, 600);
        getContentPane().setBackground(mainColor);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setShit();
    }

    private void setShit() {
        usernameLabel = new JLabel("Nombre de Usuario");
        usernameLabel.setBounds(180, 50, 200, 40);
        username = new JTextField();
        username.setBounds(180, 100, 200, 40);

        passwordLabel = new JLabel("Contraseña");
        passwordLabel.setBounds(180, 150, 200, 40);
        password = new JPasswordField();
        password.setBounds(180, 200, 200, 40);

        send = new JButton("Iniciar Sesión");
        send.setBounds(210, 280, 140, 40);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result = "";
                nombre = username
                        .getText();
                contrasena = password
                        .getText();
                current.enviarLogIn(nombre, contrasena);
                try {
                    DataInputStream in = new DataInputStream(current.client.getInputStream());
                    result = in.readUTF();
                } catch (IOException ex) {
                    Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (result.equals("true")) {
                    setVisible(false);
                    new Chat(nombre, current).setVisible(true);
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "DATOS ERRONEOS");
                }
            }
        });

        register = new JButton("Registrarse");
        register.setBounds(210, 330, 140, 40);
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    switchLogin();
                } catch (SQLException ex) {
                    Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(usernameLabel);
        add(passwordLabel);
        add(send);
        add(register);
        add(password);
        add(username);
    }

    //Elementos del método registro
    private JTextField nombreUsuario;
    private JTextField correo;
    private JButton regresar;
    private JButton registrarse;

    private void switchLogin() throws SQLException {
        setVisible(false);
        new RegisterScreen(current).setVisible(true);
        dispose();
    }

}
