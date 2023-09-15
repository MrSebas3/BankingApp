package com.schneidergroup.bankingApp.konten;

import javax.swing.*;
import java.io.Serializable;

public class SavingAccount extends BankAccount implements Serializable {


    public SavingAccount(String accountNumber, double bankBalance, Customer customer, String accountName) {
        super(accountNumber, bankBalance, customer, accountName);
    }

    @Override
    public boolean withdraw(double amount, BankAccount ExternalAccount, String message){
        JOptionPane.showMessageDialog(null, "Von einem Festgeldkonto kann kein Geld abgehoben werden!", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}