package gui;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import java.awt.Color;

import org.json.JSONObject;

import TestClass.JTextFieldHintUI;
import TestClass.JPassFieldHintUI;
import TestClass.RoundedJTextField;
import TestClass.RoundedJPassField;

public class LoginScreen extends JFrame implements ActionListener {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private JLabel nameLabel, passLabel;
    private RoundedJTextField nameBox;
    private JButton loginButton, signupButton;
    private RoundedJPassField passBox;
    private JCheckBox showPassword;

    private int status;

    private Socket socket;

    public LoginScreen(Socket socket) {
        super("Login");
        status = 0;
        init(socket);
    }

    private void init(Socket socket) {

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

        passLabel = new JLabel("Password");
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
        loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
        loginButton.setBorder(new LineBorder(Color.BLACK));
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.LIGHT_GRAY);
        loginButton.setBounds(270, 300, 100, 30);
        loginButton.addActionListener(this);
        add(loginButton);

        signupButton = new JButton("Signup");
        signupButton.setFocusPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setOpaque(true);
        signupButton.setBorder(new LineBorder(Color.BLACK));
        signupButton.setBackground(Color.DARK_GRAY);
        signupButton.setForeground(Color.LIGHT_GRAY);
        signupButton.setBounds(150, 300, 100, 30);
        signupButton.addActionListener(this);
        add(signupButton);

        // Socket configuration
        this.socket = socket;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == loginButton) {
            System.out.println("Login Click");
            try {
                onClickLogin();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (actionEvent.getSource() == signupButton) {
            System.out.println("Signup Click");
            this.dispose();
            SingUpScreen singUpScreen = new SingUpScreen(this.socket);
            singUpScreen.setVisible(true);
        } else if (actionEvent.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passBox.setEchoChar((char) 0);
            } else {
                passBox.setEchoChar('•');
            }
        }
    }

    private void onClickLogin() throws IOException {
        String name = nameBox.getText();
        String pass = new String(passBox.getPassword());

        Map<String, String> map = new HashMap<>();
        map.put("op", "1");
        map.put("name", name);
        map.put("pass", pass);

        JSONObject json = new JSONObject(map);
        String jsonString = json.toString();
        socket.getOutputStream().write(jsonString.getBytes());
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
