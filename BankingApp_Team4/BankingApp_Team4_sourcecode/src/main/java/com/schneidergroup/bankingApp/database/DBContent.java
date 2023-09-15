package com.schneidergroup.bankingApp.database;

import com.schneidergroup.bankingApp.konten.Customer;

import java.io.Serializable;
import java.util.ArrayList;

public class DBContent implements Serializable {
    public ArrayList<Customer> CustomerAccounts = new ArrayList<>();
}
