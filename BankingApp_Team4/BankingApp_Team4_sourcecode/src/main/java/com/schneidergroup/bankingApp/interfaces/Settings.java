package com.schneidergroup.bankingApp.interfaces;

import com.schneidergroup.bankingApp.database.DBContent;
import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.konten.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Settings extends JFrame {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "(\\+)?\\d{1,}-?\\d{3}-?\\d{3}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    public Settings() {
        setTitle("Einstellungen");
        setLayout(new GridBagLayout());
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Vor- und Nachname: ");
        JTextField nameField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 0;
        nameField.setText(DBProvider.getInstance().getCurrentCustomer().getName());
        add(nameLabel, c);
        c.gridx = 1;
        add(nameField, c);

        JLabel addressLabel = new JLabel("Adresse: ");
        JTextField addressField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 2;
        addressField.setText(DBProvider.getInstance().getCurrentCustomer().getAddress());
        add(addressLabel, c);
        c.gridx = 1;
        add(addressField, c);

        JLabel emailLabel = new JLabel("E-Mail: ");
        JTextField emailField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 3;
        emailField.setText(DBProvider.getInstance().getCurrentCustomer().getEmail());
        add(emailLabel, c);
        c.gridx = 1;
        add(emailField, c);

        JLabel phoneLabel = new JLabel("Telefonnummer: ");
        JTextField phoneField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 4;
        phoneField.setText(DBProvider.getInstance().getCurrentCustomer().getPhoneNumber());
        add(phoneLabel, c);
        c.gridx = 1;
        add(phoneField, c);

        JLabel usernameLabel = new JLabel("Benutzername: ");
        JTextField usernameField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 5;
        usernameField.setText(DBProvider.getInstance().getCurrentCustomer().getUsername());
        add(usernameLabel, c);
        c.gridx = 1;
        add(usernameField, c);

        JLabel passwordLabel = new JLabel("Passwort: ");
        JPasswordField passwordField = new JPasswordField(10);
        c.gridx = 0;
        c.gridy = 6;
        add(passwordLabel, c);
        c.gridx = 1;
        add(passwordField, c);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> setVisible(false));
        JButton submitButton = new JButton("Senden");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 2;
        add(buttonPanel, c);

        pack();
        setVisible(true);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String phoneNum = phoneField.getText();
                if(!isValidPhoneNumber(phoneNum)){
                    JOptionPane.showMessageDialog(null, "Ungültige Telefonnummer!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String password = new String(passwordField.getPassword());
                String password1 = DBProvider.getInstance().getCurrentCustomer().getPassword();
                if (!passwordField.getText().isEmpty()) {
                    if (passwordField.getText().equals(password1)) {
                        // Das Passwort wurde nicht geändert
                    } else {
                        DBProvider.getInstance().getCurrentCustomer().setPassword(password);
                    }
                }

                String username1 = DBProvider.getInstance().getCurrentCustomer().getUsername();
                if (!usernameField.getText().isEmpty()) {
                    if (usernameField.getText().equals(username1)) {
                        // Der Benutzername wurde nicht geändert
                    } else {
                        DBProvider.getInstance().getCurrentCustomer().setUsername(usernameField.getText());
                    }
                }

                String address = DBProvider.getInstance().getCurrentCustomer().getAddress();
                if (!addressField.getText().isEmpty()) {
                    if (addressField.getText().equals(address)) {
                        // Die Adresse wurde nicht geändert
                    } else {
                        DBProvider.getInstance().getCurrentCustomer().setAddress(addressField.getText());
                    }
                }

                String name1 = DBProvider.getInstance().getCurrentCustomer().getName();
                if (!nameField.getText().isEmpty()) {
                    if (nameField.getText().equals(name1)) {
                        // Der Name wurde nicht geändert
                    } else {
                        DBProvider.getInstance().getCurrentCustomer().setName(nameField.getText());
                    }
                }

                String email1 = DBProvider.getInstance().getCurrentCustomer().getEmail();
                if (!emailField.getText().isEmpty()) {
                    if (emailField.getText().equals(email1)) {
                        // Die E-Mail wurde nicht geändert
                    } else {
                        DBProvider.getInstance().getCurrentCustomer().setEmail(emailField.getText());
                    }
                }

                String phoneNumber1 = DBProvider.getInstance().getCurrentCustomer().getPhoneNumber();
                if (!phoneField.getText().isEmpty()) {
                    if (phoneField.getText().equals(phoneNumber1)) {
                        // Die Telefonnummer wurde nicht geändert
                    } else {
                        DBProvider.getInstance().getCurrentCustomer().setPhoneNumber(phoneField.getText());
                    }
                }

                JOptionPane.showMessageDialog(null, "Erfolgreich geändert!");
                setVisible(false);


                DBProvider.getInstance().save();
            }

        });

    }
}
