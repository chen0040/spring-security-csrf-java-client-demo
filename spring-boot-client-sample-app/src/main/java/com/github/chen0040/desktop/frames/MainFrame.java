package com.github.chen0040.desktop.frames;


import com.github.chen0040.springclient.service.SpringBootClient;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private JLabel statusLabel;

    public MainFrame() {
        super("Spring Security ");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(800, 600);

        JMenuBar menuBar = new JMenuBar();

        JMenu actionMenu = new JMenu("Action");
        menuBar.add(actionMenu);
        this.setJMenuBar(menuBar);

        // create the status bar panel and shove it down the bottom of the frame
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        this.setLocationRelativeTo(null);
    }


    private void loadContent() {
        setVisible(true);
    }

    public void run(){


        if(SpringBootClient.getSingleton().isAuthenticated()) {
            loadContent();
        } else {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.authenticate(account -> {
                loadContent();
            });
        }
    }
}
