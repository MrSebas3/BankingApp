package com.schneidergroup.bankingApp.interfaces;

import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.konten.BankAccount;
import com.schneidergroup.bankingApp.konten.Customer;
import com.schneidergroup.bankingApp.konten.GiroAccount;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class BankerUI extends JFrame {

    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JList<Customer> customerList;
    private JList<BankAccount> bankAccountList;
    private JTextField accountLimitField;
    private JButton setLimitButton;
    private JCheckBox approveCheckBox;
    private JPanel accountPanel;
    private JTextArea accountDetailsText;

    public enum FilterType{
        All,
        Pending,
        Negative
    }

    private FilterType getCurrentFilterType(){
        FilterType filter = FilterType.All;
        if(filterComboBox.getSelectedItem() == null) return filter;
        switch (filterComboBox.getSelectedItem().toString()){
            case "Ausstehend":
                filter = FilterType.Pending;
                break;
            case "Negativ":
                filter = FilterType.Negative;
                break;
        }
        return filter;
    }

    private List<Customer> filterByParamters(List<Customer> customers, FilterType filter){
        List<Customer> filtered = new ArrayList<>();
        String searchQuery = searchField.getText();

        for(Customer c : customers) {
            if(!searchQuery.isEmpty() && !c.getName().contains(searchQuery))
                continue;

            if(filter == FilterType.All){
                filtered.add(c);
                continue;
            }

            for (BankAccount ba : c.getAllAccounts()) {
                if(filter == FilterType.Pending && !ba.getApproved()) {
                    filtered.add(c);
                    break;
                }else if(filter == FilterType.Negative && ba.getBankBalance() < 0){
                    filtered.add(c);
                    break;
                }
            }
        }

        return filtered;
    }

    private List<BankAccount> filterAccountsByParams(List<BankAccount> accounts, FilterType filter){
        if(filter == FilterType.All) return accounts;

        List<BankAccount> filtered = new ArrayList<>();

        for (BankAccount ba : accounts) {
            if(filter == FilterType.Pending && !ba.getApproved()) {
                filtered.add(ba);
            }else if(filter == FilterType.Negative && ba.getBankBalance() < 0){
                filtered.add(ba);
            }
        }

        return filtered;
    }

    BankAccount SelectedAccount = null;

    private void refreshAll(){
        refreshCustomers();
        refreshBankAccounts();
        refreshBankAccountInfo();
    }

    private void refreshBankAccountInfo(){
        SelectedAccount = bankAccountList.getSelectedValue();
        if(SelectedAccount != null) {

            String output = "Name: " + SelectedAccount.getAccountName();
            output +="\nTyp: " + ((SelectedAccount instanceof GiroAccount) ? "Girokonto" : "Festgeldkonto");
            output += "\nKontostand: "  + SelectedAccount.getBankBalance() + "€";
            output += "\nIBAN: " + SelectedAccount.getAccountNumber();

            accountLimitField.setVisible(SelectedAccount instanceof GiroAccount);
            accountLimitField.setText((SelectedAccount instanceof GiroAccount) ? String.valueOf(((GiroAccount)SelectedAccount).getDispo()) : "");
            setLimitButton.setVisible(SelectedAccount instanceof GiroAccount);
            accountDetailsText.setText(output);
            approveCheckBox.setSelected(SelectedAccount.getApproved());
            accountPanel.setVisible(true);
        }else{
            accountPanel.setVisible(false);
        }
    }

    private void refreshCustomers(){
        DefaultListModel<Customer> customerDefaultListModel = new DefaultListModel<>();
        customerDefaultListModel.addAll(filterByParamters(DBProvider.getInstance().getCustomers(), getCurrentFilterType()));
        customerList.setModel(customerDefaultListModel);
    }

    private void refreshBankAccounts(){
        Customer selectedCustomer = customerList.getSelectedValue();
        if(selectedCustomer != null) {
            DefaultListModel<BankAccount> accountDefaultListModel = new DefaultListModel<>();

            accountDefaultListModel.addAll(filterAccountsByParams(selectedCustomer.getAllAccounts(), getCurrentFilterType()));

            bankAccountList.setModel(accountDefaultListModel);
        }else{
            bankAccountList.setListData(new BankAccount[0]);
        }
    }


    public BankerUI() {
        setTitle("Banker");
        setSize(500,900);

        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        searchField = new JTextField(20);
        filterComboBox = new JComboBox<>(new String[]{"Alle", "Ausstehend", "Negativ"});
        topPanel.add(searchField);
        topPanel.add(filterComboBox);

        // Middle Panel
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(1, 3));
        customerList = new JList<>();
        bankAccountList = new JList<>();
        accountLimitField = new JTextField(10);
        setLimitButton = new JButton("Limit setzen");
        approveCheckBox = new JCheckBox("Freigegeben");

        refreshCustomers();
        customerList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                refreshBankAccounts();
            }
        });

        setLimitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double limit = Double.parseDouble(accountLimitField.getText());
                    ((GiroAccount)SelectedAccount).setDispo(limit);
                    DBProvider.getInstance().save();
                    JOptionPane.showMessageDialog(null, "Limit wurde erfolgreich gesetzt!");

                }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, "Limit ist keine gültige Zahl!");
                }
            }
        });

        approveCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectedAccount.setApproved(approveCheckBox.isSelected());
                DBProvider.getInstance().save();
            }
        });

        bankAccountList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                refreshBankAccountInfo();
            }
        });

        accountPanel = new JPanel();
        accountPanel.setLayout(new BorderLayout());

        accountDetailsText = new JTextArea();
        accountDetailsText.setEditable(false);
        accountDetailsText.setText("Konto wählen!");

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        detailsPanel.add(accountDetailsText);

        accountPanel.add(detailsPanel, BorderLayout.NORTH);

        JPanel limitPanel = new JPanel();
        limitPanel.setLayout(new FlowLayout());
        limitPanel.add(accountLimitField);
        limitPanel.add(setLimitButton);
        accountPanel.add(limitPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(approveCheckBox);

        accountPanel.add(buttonPanel, BorderLayout.SOUTH);

        middlePanel.add(new JScrollPane(customerList));
        middlePanel.add(new JScrollPane(bankAccountList));
        middlePanel.add(accountPanel);

        accountPanel.setVisible(false);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAll();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshAll();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshAll();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshAll();
            }

        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                new Login();
            }
        });

    }
}



