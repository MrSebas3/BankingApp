package com.schneidergroup.bankingApp.konten;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount implements Serializable {
    private String AccountNumber;
    private double AccountBalance;
    private String AccountName;
    private Customer Customer;
    private boolean Approved = false;
    protected List<Transaction> Transactions;


    public BankAccount(String accountNumber, double bankBalance, Customer customer, String accountName){
        this.AccountNumber = accountNumber;
        this.AccountBalance = bankBalance;
        this.Customer = customer;
        this.AccountName = accountName;
        Transactions = new ArrayList<Transaction>();
    }

    // dummy method -> to be overridden
    public boolean withdraw(double amount, BankAccount target, String message){return false;}

    public void deposit(double amount, BankAccount source, String message){
        Transaction t = new Transaction(true, source, amount, LocalDateTime.now(), message);
        Transactions.add(t);
        setAccountBalance(getBankBalance() + amount);
    }

    public String getAccountNumber(){
        return AccountNumber;
    }

    public double getBankBalance(){
        return AccountBalance;
    }

    public void setAccountBalance(double bankBalance) {
        AccountBalance = bankBalance;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setApproved(boolean bool){
        Approved = bool;
    }
    public boolean getApproved(){
        return Approved;
    }

    public List<Transaction> getTransactions(){
        return Transactions;
    }

    @Override
    public String toString(){
        return this.getAccountName();
    }
}