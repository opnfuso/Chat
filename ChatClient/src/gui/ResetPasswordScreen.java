/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import TestClass.JPassFieldHintUI;
import TestClass.JTextFieldHintUI;
import TestClass.RoundedJPassField;
import TestClass.RoundedJTextField;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.LineBorder;
import org.json.JSONObject;

/**
 *
 * @author snowb
 */
public class ResetPasswordScreen extends JFrame implements ActionListener {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    
    private JLabel nameLabel, passLabel;
    private RoundedJTextField nameBox;
    private JButton ResetPButton;
    private RoundedJPassField passBox;
    private JCheckBox showPassword;
    
    private Socket socket;
    private int status;
    
    public ResetPasswordScreen(Socket socket) {
        super("ResetPasswordScreen");
        status = 0;
        init(socket);
    }
    
    private void init(Socket socket){
      // General screen options
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(38, 38, 38));
        
        // Form.
        nameLabel = new JLabel("Username");
        nameLabel.setForeground(Color.LIGHT_GRAY);
        nameLabel.setLocation(200, 140);
        nameLabel.setSize(100, 25);
        add(nameLabel);

        nameBox = new RoundedJTextField();
        nameBox.setSelectedTextColor(Color.DARK_GRAY);
        nameBox.setBounds(200, 170, 100, 30);
        nameBox.setUI(new JTextFieldHintUI("Ur Username", Color.GRAY));
        add(nameBox);

        passLabel = new JLabel("New Password");
        passLabel.setForeground(Color.LIGHT_GRAY);
        passLabel.setLocation(200, 200);
        passLabel.setSize(100, 25);
        add(passLabel);

        passBox = new RoundedJPassField();
        passBox.setSelectedTextColor(Color.DARK_GRAY);
        passBox.setBounds(200, 230, 100, 30);
        passBox.setUI(new JPassFieldHintUI("Ur Password", Color.GRAY));
        passBox.setEchoChar('•');
        add(passBox);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(200, 270, 120, 30);
        showPassword.setBackground(new Color(38, 38, 38));
        showPassword.setForeground(Color.LIGHT_GRAY);
        showPassword.setOpaque(true);
        showPassword.addActionListener(this);
        add(showPassword);

        // Controllers.
        ResetPButton = new JButton("Reset Password");
        ResetPButton.setFocusPainted(false);
        ResetPButton.setContentAreaFilled(false);
        ResetPButton.setOpaque(true);
        ResetPButton.setBorder(new LineBorder(Color.BLACK));
        ResetPButton.setBackground(Color.DARK_GRAY);
        ResetPButton.setForeground(Color.LIGHT_GRAY);
        ResetPButton.setBounds(200, 300, 100, 30);
        ResetPButton.addActionListener(this);
        add(ResetPButton);


        // Socket configuration
        this.socket = socket;
    }
    
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passBox.setEchoChar((char) 0);
            } else {
                passBox.setEchoChar('•');
            }
        } 
        else if (actionEvent.getSource() == ResetPButton) {
            try {
                onClickResetPassword();
                this.dispose();
                LoginScreen loginScreen = new LoginScreen(this.socket);
                loginScreen.setVisible(true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } 
        
    }
    
    private void onClickResetPassword() throws IOException {
        String name = nameBox.getText();
        String pass = new String(passBox.getPassword());

        Map<String, String> map = new HashMap<>();
        map.put("op", "4");
        map.put("name", name);
        map.put("pass", pass);

        JSONObject json = new JSONObject(map);
        String jsonString = json.toString();
        socket.getOutputStream().write(jsonString.getBytes());
    }
    
}
