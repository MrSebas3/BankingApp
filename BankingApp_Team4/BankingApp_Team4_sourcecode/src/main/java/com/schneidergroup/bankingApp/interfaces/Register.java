package com.schneidergroup.bankingApp.interfaces;

import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.konten.Customer;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends JFrame {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "(\\+)?\\d{1,}-?\\d{3}-?\\d{3}-?\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    public Register() {
        setTitle("Registrierung");
        setLayout(new GridBagLayout());
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        JLabel nameLabel = new JLabel("Vor- und Nachname: ");
        JTextField nameField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 0;
        add(nameLabel, c);
        c.gridx = 1;
        add(nameField, c);

        JLabel birthDateLabel = new JLabel("Geburtsdatum: ");
        JTextField birthDateField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 1;
        add(birthDateLabel, c);
        c.gridx = 1;
        add(birthDateField, c);

        JLabel addressLabel = new JLabel("Adresse: ");
        JTextField addressField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 2;
        add(addressLabel, c);
        c.gridx = 1;
        add(addressField, c);

        JLabel emailLabel = new JLabel("E-Mail: ");
        JTextField emailField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 3;
        add(emailLabel, c);
        c.gridx = 1;
        add(emailField, c);

        JLabel phoneLabel = new JLabel("Telefonnummer: ");
        JTextField phoneField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 4;
        add(phoneLabel, c);
        c.gridx = 1;
        add(phoneField, c);

        JLabel usernameLabel = new JLabel("Benutzername: ");
        JTextField usernameField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 5;
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
                SimpleDateFormat dateformat = new SimpleDateFormat("dd.mm.yyyy");
                Date date = null;
                String password = new String(passwordField.getPassword());
                try {
                    date = dateformat.parse(birthDateField.getText());
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Ungültiges Datum!");
                    return;
                }

                String phoneNum = phoneField.getText();
                if(!isValidPhoneNumber(phoneNum)){
                    JOptionPane.showMessageDialog(null, "Ungültige Telefonnummer!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Customer customer = new Customer(nameField.getText(), date, addressField.getText(),emailField.getText(),phoneField.getText(),usernameField.getText(),password);
                boolean success = DBProvider.getInstance().register(customer);
                JOptionPane.showMessageDialog(null, "Erfolgreich registiert!");
                setVisible(false);
            }
        });
    }
}