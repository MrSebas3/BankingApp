package com.schneidergroup.bankingApp.database;


import com.schneidergroup.bankingApp.konten.BankAccount;
import com.schneidergroup.bankingApp.konten.Customer;

import javax.swing.*;
import java.io.*;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

public class DBProvider {
    private static DBProvider INSTANCE = null;
    private static final String DB_PATH = "db.bin";

    private String AbsoluteDatabasePath = "";
    private DBContent Database;

    private Customer CurrentLogin;

    private DBProvider(DBContent content, String dbPath){
        Database = content;
        AbsoluteDatabasePath = dbPath;
    }

    public static DBProvider getInstance(){

        if(INSTANCE != null) return INSTANCE;

        String jarPath = null;
        try {
            jarPath = new File(DBProvider.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(null, "Datenbankpfad konnte nicht gefunden werden!");
            return null;
        }

        File file = new File(jarPath, DB_PATH);

        System.out.println("Set database path -> " + file.getAbsolutePath());
        if(!file.exists()){
            INSTANCE = new DBProvider(new DBContent(), file.getAbsolutePath());
            return INSTANCE;
        }

        DBContent data = (DBContent)deserializeObject(file.getAbsolutePath());
        INSTANCE = new DBProvider(data, file.getAbsolutePath());
        return INSTANCE;
    }

    public boolean register(Customer kunde){
        Database.CustomerAccounts.add(kunde);
        return save();
    }

    public Boolean bankerLogin(String username, String password){
        if((username.equals("admin") && password.equals("1234"))
        || (username.equals("admin2") && password.equals("12345")))
            return true;
        return false;
    }

    public Customer customerLogin(String uname, String password){
        for (Customer k: Database.CustomerAccounts) {
            if(k.getLogin().equals(uname) && k.getPassword().equals(password)){
                CurrentLogin = k;
                return k;
            }
        }
        CurrentLogin = null;
        return null;
    }

    public Customer getCurrentCustomer(){
        return CurrentLogin;
    }

    public boolean save(){
        return serializeObject(Database, AbsoluteDatabasePath);
    }

    public static boolean serializeObject(Object object, String filePath) {
        try (OutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(object);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Datenbank konnte nicht gespeichert werden!");
            return false;
        }
    }

    public static Object deserializeObject(String filePath) {
        try (InputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Datenbank konnte nicht geladen werden!");
        }
        return null;
    }

    public BankAccount findBankAccountbyIban(String iban){
        for (Customer customer : Database.CustomerAccounts){
            if(customer.getAccount(iban) != null)
                return customer.getAccount(iban);
        }
        return null;
    }

    public List<Customer> getCustomers(){
        return this.Database.CustomerAccounts;
    }

}


