package com.github.chen0040.desktop.frames;

import com.github.chen0040.springclient.service.SpringBootClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class LoginFrame  extends JFrame implements ActionListener
{
    JButton btnLogin;
    JPanel panel;
    JLabel lblUsername, lblPassword;
    final JTextField txtUsername, txtPassword;
    private Consumer<Boolean> onAuthentication;

    public void setOnAuthentication(Consumer<Boolean> callback) {
        this.onAuthentication = callback;
    }


    public LoginFrame()
    {
        lblUsername = new JLabel();
        lblUsername.setText("Username:");
        txtUsername = new JTextField(15);

        lblPassword = new JLabel();
        lblPassword.setText("Password:");
        txtPassword = new JPasswordField(15);

        btnLogin =new JButton("Login");

        panel=new JPanel(new GridLayout(3,1));
        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        add(panel,BorderLayout.CENTER);
        btnLogin.addActionListener(this);
        setTitle("LOGIN FORM");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);
    }

    public void authenticate(Consumer<Boolean> callback) {
        txtUsername.setText("admin");
        txtPassword.setText("admin");

        setOnAuthentication(callback);
        setSize(400, 200);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae)
    {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        SpringBootClient.getSingleton().login(username, password, authenticated -> {
            setVisible(false);
            onAuthentication.accept(authenticated);
        });
    }
}

