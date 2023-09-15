package com.schneidergroup.bankingApp.konten;


import com.schneidergroup.bankingApp.database.DBProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer extends Person implements Serializable {
    private String login;
    private String password;
    private List<BankAccount> accounts;

    public Customer(String name, Date birthDate, String address, String email, String phoneNumber, String login, String password) {
        super();
        this.setName(name);
        this.setBirthDate(birthDate);
        this.setAddress(address);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this.login = login;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    public void login(String login, String password) {
        if (this.login.equals(login) && this.password.equals(password)) {
            // Der Benutzer ist erfolgreich eingeloggt
        } else {
            // Falsche Login-Daten
        }
    }

    public void logout() {

    }

    public List<BankAccount> getAllAccounts(){
        return accounts;
    }

    public List<GiroAccount> allDebitAccounts(){
        List<GiroAccount> giroAccounts = new ArrayList<>();
        for (BankAccount konto : accounts){
            if(konto instanceof GiroAccount){
                giroAccounts.add((GiroAccount) konto);
            }
        }
        return giroAccounts;
    }

    public List<SavingAccount> allSavingAccounts(){
        List<SavingAccount> creditcards= new ArrayList<>();
        for (BankAccount konto : accounts){
            if(konto instanceof SavingAccount){
                creditcards.add((SavingAccount) konto);
            }
        }
        return creditcards;
    }
    public void createAccount(BankAccount bankAccount) {
        this.accounts.add(bankAccount);
        DBProvider.getInstance().save();
    }

    public void deleteAccount(BankAccount account) {
        this.accounts.remove(account);
    }



    public BankAccount getAccount(String kontoNummer) {
        for (BankAccount bankAccount : this.accounts) {
            if (bankAccount.getAccountNumber().equals(kontoNummer)) {
                return bankAccount;
            }
        }
        return null;
    }


    public boolean isAccountCoverd(String kontoNummer, double betrag) {
        for (BankAccount bankAccount : this.accounts) {
            if (bankAccount.getAccountNumber().equals(kontoNummer)) {
                return bankAccount.getBankBalance() >= betrag;
            }
        }
        return false;
    }


    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setUsername(String username){
        login = username;
    }

    public String getUsername(){
        return login;
    }
    public String getLogin(){
        return login;
    }

    @Override
    public String toString() {
        return super.getName();
    }
}