package com.schneidergroup.bankingApp.interfaces;

import com.schneidergroup.bankingApp.database.DBProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JButton customerLoginButton;
    private JButton bankerLoginButton;
    private JButton registerButton;

    public Login() {
        setTitle("Banking App");
        setSize(600,80);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 3, 10, 10)); // 1 row, 3 columns, 10-pixel gap

        createAndAddComponents();

        setLocationRelativeTo(null); // Center the UI on the screen
        setVisible(true);
    }

    // Move the creation and adding of components to a separate method
    private void createAndAddComponents() {
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        leftPanel.setSize(200,80);
        customerLoginButton = new JButton("Kunde-Login");
        leftPanel.add(customerLoginButton);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setSize(200,80);
        registerButton = new JButton("Registrieren");
        centerPanel.add(registerButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightPanel.setSize(200,80);
        bankerLoginButton = new JButton("Bänker-Login");
        rightPanel.add(bankerLoginButton);

        add(leftPanel);
        add(centerPanel);
        add(rightPanel);

        // Set ActionListener for customerLoginButton
        customerLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createCustomerLoginWindow();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register();
            }
        });
        // Set ActionListener for bankerLoginButton
        bankerLoginButton.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createBankerLoginWindow();
            }
        }));
    }



    private void createBankerLoginWindow() {
        JFrame loginFrame = new JFrame("Bänker-Login");
        loginFrame.setSize(400,200);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Benutzername:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateBanker(username, password)) {
                    loginFrame.setVisible(false);
                    new BankerUI();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Falscher Benutzername oder Passwort!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(new JLabel()); // Placeholder
        loginFrame.add(loginButton);


        loginFrame.setLocationRelativeTo(null); // Center the login window
        loginFrame.setVisible(true);
    }


    private void createCustomerLoginWindow() {
        JFrame loginFrame = new JFrame("Kunden-Login");
        loginFrame.setSize(400,200);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Benutzername:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Passwort:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (authenticateUser(username, password)) {
                    loginFrame.setVisible(false);
                    new UserInterface();
                    setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Falscher Benutzername oder Passwort!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });

        loginFrame.add(usernameLabel);
        loginFrame.add(usernameField);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordField);
        loginFrame.add(new JLabel()); // Placeholder
        loginFrame.add(loginButton);


        loginFrame.setLocationRelativeTo(null); // Center the login window
        loginFrame.setVisible(true);
    }

    private static boolean authenticateUser(String username, String password) {
        return DBProvider.getInstance().customerLogin(username,password) != null;
    }

    private static boolean authenticateBanker(String username, String password) {
        return DBProvider.getInstance().bankerLogin(username, password)  != null;

    }
}
