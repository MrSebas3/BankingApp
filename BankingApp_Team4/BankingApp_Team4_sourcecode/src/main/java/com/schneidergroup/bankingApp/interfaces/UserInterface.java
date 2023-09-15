package com.schneidergroup.bankingApp.interfaces;
import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.konten.BankAccount;
import com.schneidergroup.bankingApp.konten.GiroAccount;
import com.schneidergroup.bankingApp.konten.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame {

    JList<BankAccount> AccountListElement;
    JTextArea AccountDetailsText;
    UserInterface CurrentInstance;

    public void refreshAccountsCallback(){
        DefaultListModel<BankAccount> accountListModel = new DefaultListModel<>();

        accountListModel.addAll(DBProvider.getInstance().getCurrentCustomer().allSavingAccounts());
        accountListModel.addAll(DBProvider.getInstance().getCurrentCustomer().allDebitAccounts());

        AccountListElement.setModel(accountListModel);

    }

    public void refreshAccountInfo(){
        BankAccount selectedAccount = AccountListElement.getSelectedValue();
        if(selectedAccount!= null){
            String output = "Name: " + selectedAccount.getAccountName();
            output +="\nTyp: " + ((selectedAccount instanceof GiroAccount) ? "Girokonto" : "Festgeldkonto");
            output += "\nKontostand: "  + selectedAccount.getBankBalance() + "€";
            output += "\nIBAN: " + selectedAccount.getAccountNumber();
            output += (selectedAccount instanceof GiroAccount) ? "\nMax Dispo: " + ((GiroAccount)selectedAccount).getDispo() + "€" : "";

            AccountDetailsText.setText(output);
        }else{
            AccountDetailsText.setText("Konto wählen!");
        }
    }

    public UserInterface() {
            CurrentInstance = this;
            setTitle("Banking App");

            setSize(900, 400);


            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.8;

            JPanel leftPanel = new JPanel();
            leftPanel.setBorder(BorderFactory.createTitledBorder("Konten"));

            AccountListElement = new JList<>();
            refreshAccountsCallback();
            leftPanel.add(AccountListElement);

            add(leftPanel, c);

            c.gridx = 1;
            JPanel rightPanel = new JPanel();
            rightPanel.setBorder(BorderFactory.createTitledBorder("Kontodetails"));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            AccountDetailsText = new JTextArea();
            AccountDetailsText.setEditable(false);
            AccountDetailsText.setText("Konto wählen!");
            add(AccountDetailsText);
            add(rightPanel, c);

            c.gridx = 0;
            c.gridy = 1;
            c.weighty = 0.2;
            JPanel bottomLeftPanel = new JPanel();
            JButton newAccount = new JButton("Neues Konto erstellen");
            JButton transfer = new JButton("Überweisung");
            JButton deleteAccount = new JButton("Konto Löschen");
            JButton TransferHistory = new JButton("Transferhistorie");
            bottomLeftPanel.add(deleteAccount);
            bottomLeftPanel.add(newAccount);
            bottomLeftPanel.add(transfer);
            bottomLeftPanel.add(TransferHistory);
            add(bottomLeftPanel, c);

            //Überweisungsbutton Logik
            transfer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    BankAccount selectedAccount = AccountListElement.getSelectedValue();
                    if (selectedAccount == null){
                        JOptionPane.showMessageDialog(CurrentInstance, "Bitte wähle ein Konto aus!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }else {
                        new Transfer(selectedAccount, CurrentInstance);
                    }
                }
            });

            TransferHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    BankAccount selectedAccount = AccountListElement.getSelectedValue();
                    if (selectedAccount == null){
                        JOptionPane.showMessageDialog(CurrentInstance, "Bitte wähle ein Konto aus!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }else {


                        JFrame transfer = new JFrame("Transferhistorie");

                        transfer.setPreferredSize(new Dimension(400, 300));

                        // Create components
                        JLabel label = new JLabel("Transaktionen:");
                        DefaultListModel<Transaction> listModel = new DefaultListModel<>();
                        listModel.addAll(selectedAccount.getTransactions());
                        JList<Transaction> transactionList = new JList<>(listModel);
                        JScrollPane scrollPane = new JScrollPane(transactionList);

                        // Add components to the frame
                        Container contentPane = transfer.getContentPane();
                        contentPane.setLayout(new BorderLayout());
                        contentPane.add(label, BorderLayout.NORTH);
                        contentPane.add(scrollPane, BorderLayout.CENTER);

                        transfer.pack();
                        transfer.setVisible(true);
                    }

                }
            });

            deleteAccount.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    BankAccount selectedAccount = AccountListElement.getSelectedValue();
                    if (selectedAccount == null){
                        JOptionPane.showMessageDialog(CurrentInstance, "Bitte wähle ein Konto aus!", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }else {
                        DBProvider.getInstance().getCurrentCustomer().deleteAccount(selectedAccount);
                        DBProvider.getInstance().save();
                        refreshAccountsCallback();
                        refreshAccountInfo();
                    }
                }
            });

            AccountListElement.addListSelectionListener(e -> {
                refreshAccountInfo();
            });

            newAccount.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new CreateAccount(CurrentInstance);
                }
            });



            c.gridx = 1;
            JPanel bottomRightPanel = new JPanel();
            JButton settingsButtons = new JButton("Einstellungen");
            settingsButtons.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Settings();
                }
            });
            bottomRightPanel.add(settingsButtons);
            add(bottomRightPanel, c);


            setLocationRelativeTo(null);
            setVisible(true);

            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    new Login();
                }
            });
    }
}






















































