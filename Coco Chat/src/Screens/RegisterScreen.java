/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Screens;

import Client.Client;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * @author usuario
 */
public class RegisterScreen extends JFrame {

    private Color mainColor = new Color(250, 242, 255);
    private JTextField username;
    private JPasswordField password;
    private JTextField name;
    private JButton back;
    private JButton register;
    DataOutputStream write;
    private Client current;

    public RegisterScreen(Client client) {
        init();
        current = client;
    }

    private void init() {
        JLabel userLabel = new JLabel("Correo");
        JLabel nameLabel = new JLabel("Nombre");
        JLabel passLabel = new JLabel("Contraseña");
        setLocation(566, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(mainColor);
        this.setSize(600, 600);
        setTitle("Registro");
        setLayout(null);
        userLabel.setBounds(180, 50, 200, 40);
        username = new JTextField();
        username.setBounds(180, 80, 200, 40);

        nameLabel.setBounds(180, 130, 200, 40);
        name = new JTextField();
        name.setBounds(180, 160, 200, 40);

        passLabel.setBounds(180, 210, 200, 40);
        password = new JPasswordField();
        password.setBounds(180, 240, 200, 40);

        register = new JButton("Registrate");
        register.setBounds(210, 320, 140, 40);
        back = new JButton("Volver");
        back.setBounds(210, 370, 140, 40);
        add(userLabel);
        add(nameLabel);
        add(passLabel);
        add(back);
        add(register);
        add(password);
        add(username);
        add(name);
        setButtonListeners();
    }

    private void setButtonListeners() {
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    switchLogin();
                } catch (SQLException ex) {
                    Logger.getLogger(RegisterScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pushUser();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

        });
    }

    private void switchLogin() throws SQLException {
        setVisible(false);
        new LoginScreen(current).setVisible(true);
        dispose();
    }

    private void pushUser() throws SQLException {
        String name = this.name.getText().toString();
        String password = this.password.getText().toString();
        String username = this.username.getText().toString();
        if (name.equals("") || password.equals("") || username.equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Campos vacíos");
            return;
        }
        registrar(name, username, password);
        JOptionPane.showMessageDialog(rootPane, "Registrado con éxito");
        switchLogin();
    }

    private void registrar(String name, String email, String password) throws SQLException {
        current.enviarRegister(name, email, password);
    }

}
