package com.schneidergroup.bankingApp.konten;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {

    private boolean Incoming;
    private BankAccount ExternalAccount;
    private double Amount;
    private LocalDateTime Timestamp;
    private String Message;

    public Transaction(boolean incoming, BankAccount externalAccount, double amount, LocalDateTime timestamp, String message){
        this.Incoming = incoming;
        this.ExternalAccount = externalAccount;
        this.Amount = amount;
        this.Timestamp = timestamp;
        this.Message = message;
    }

    public boolean isIncoming(){return Incoming;};
    public BankAccount getExternalAccount(){return ExternalAccount;};
    public double getAmount(){return Amount;};
    public LocalDateTime getTimestamp(){return Timestamp;};

    @Override
    public String toString(){
        return Amount + "€ " + (Incoming ? "received from " : "sent to ") + ExternalAccount.getAccountNumber() + ": " + Message;
    }
}
