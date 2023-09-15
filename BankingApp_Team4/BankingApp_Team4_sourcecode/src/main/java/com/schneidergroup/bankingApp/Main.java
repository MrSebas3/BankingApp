package com.schneidergroup.bankingApp;

import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.interfaces.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DBProvider inst = DBProvider.getInstance(); // init db
                new Login();
            }
        });

    }
}