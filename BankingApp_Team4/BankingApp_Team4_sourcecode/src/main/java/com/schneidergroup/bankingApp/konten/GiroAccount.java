package com.schneidergroup.bankingApp.konten;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;

public class GiroAccount extends BankAccount implements Serializable {
    private double Dispo;

    public GiroAccount(String accountnumber, double balance, Customer customer, String accountname, double dispo) {
        super(accountnumber, balance, customer, accountname);
        this.Dispo = dispo;

    }

    @Override
    public boolean withdraw(double amount, BankAccount target, String message) {

        if(!getApproved()){
            JOptionPane.showMessageDialog(null, "Ihr Konto wurde noch nicht vom Banker genehmigt, bitte warten Sie bevor Sie Geld überweisen.");
            return false;
        }

        double bankBalance = getBankBalance();
        double dispotUsage = 0;

        if (amount > (bankBalance + Dispo)) {
            JOptionPane.showMessageDialog(null, "Sie haben nicht genügend Guthaben und Dispositionskredit!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            double newBalance = bankBalance - amount;

            if (newBalance < 0) {
                dispotUsage = -newBalance;
            } else if (bankBalance > 0) {
                dispotUsage = bankBalance - newBalance;
            }

            Transaction t = new Transaction(false, target, amount, LocalDateTime.now(), message);
            Transactions.add(t);

            setAccountBalance(newBalance);
            JOptionPane.showMessageDialog(null, dispotUsage + "€ vom Dispotionskredit werden für die Transaktion verwendet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
    }

    public double getDispo(){return Dispo;}
    public void setDispo(double value){Dispo = value;}
}