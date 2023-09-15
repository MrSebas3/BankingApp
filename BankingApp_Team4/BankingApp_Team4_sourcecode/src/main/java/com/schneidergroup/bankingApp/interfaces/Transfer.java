package com.schneidergroup.bankingApp.interfaces;

import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.konten.BankAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Transfer extends JFrame {

    public Transfer(BankAccount selectedAccount, UserInterface parentComponent) {
        setTitle("Überweisung");
        setLayout(new GridBagLayout());
        setSize(500, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GridBagConstraints c = new GridBagConstraints();

        JLabel selectedAccountLabel = new JLabel("Ausgewähltes Konto: " + selectedAccount.toString());
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(selectedAccountLabel, c);

        JLabel recipientLabel = new JLabel("Empfänger: ");
        JTextField recipientField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(recipientLabel, c);
        c.gridx = 1;
        add(recipientField, c);

        JLabel amountLabel = new JLabel("Betrag: ");
        JTextField amountField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 2;
        add(amountLabel, c);
        c.gridx = 1;
        add(amountField, c);

        JLabel purposeLabel = new JLabel("Verwendungszweck: ");
        JTextField purposeField = new JTextField(10);
        c.gridx = 0;
        c.gridy = 3;
        add(purposeLabel, c);
        c.gridx = 1;
        add(purposeField, c);

        JButton cancelButton = new JButton("Abbrechen");
        cancelButton.addActionListener(e -> dispose());
        JButton sendButton = new JButton("Senden");
        sendButton.addActionListener(new ActionListener() {
            @Override
            // Überweisung tätigen
            public void actionPerformed(ActionEvent e) {

                String recipeint = recipientField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String purpose = purposeField.getText();
                BankAccount gesuchtesKonto = DBProvider.getInstance().findBankAccountbyIban(recipeint);

                if (gesuchtesKonto != null) {

                    boolean result = selectedAccount.withdraw(amount, gesuchtesKonto, purposeField.getText());
                    if(result) {
                        gesuchtesKonto.deposit(amount, selectedAccount, purposeField.getText());
                        DBProvider.getInstance().save();
                        parentComponent.refreshAccountInfo();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Bankkonto konnte nicht gefunden werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                setVisible(false);
                dispose();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(sendButton);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(buttonPanel, c);


        setVisible(true);
    }



}