package com.schneidergroup.bankingApp.interfaces;

import com.schneidergroup.bankingApp.database.DBProvider;
import com.schneidergroup.bankingApp.konten.BankAccount;
import com.schneidergroup.bankingApp.konten.GiroAccount;
import com.schneidergroup.bankingApp.konten.SavingAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

public class CreateAccount extends JFrame {

    public enum AccountType{
        Giro,
        SavingAccount
    }

    private AccountType CurrentAccType;
    private UserInterface ParentComponent;

    public CreateAccount(UserInterface parentComponent) {
        SwingUtilities.invokeLater(() -> {
            this.ParentComponent = parentComponent;
            JFrame frame = new JFrame("Neues Konto erstellen");
            frame.setSize(400, 200);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.setLayout(new GridLayout(4, 1));

            JLabel disclaimerLabel = new JLabel("Hinweis: Neue Kontoanträge müssen genehmigt werden, bevor sie angezeigt werden.");
            frame.add(disclaimerLabel);

            String[] accountTypes = {"Girokonto", "Festgeldkonto"};
            CurrentAccType = AccountType.Giro;

            JComboBox<String> accountTypeComboBox = new JComboBox<>(accountTypes);
            frame.add(accountTypeComboBox);

            JTextField kontonameField = new JTextField();
            JLabel kononameLabel = new JLabel("Kontoname");
            frame.add(kononameLabel);
            frame.add(kontonameField);


            JTextField dispoLimitField = new JTextField();
            JLabel LimitLabel = new JLabel("Limit");
            frame.add(LimitLabel);
            frame.add(dispoLimitField);
            dispoLimitField.setToolTipText("Gewünschtes Dispolimit eingeben");

            JButton cancelButton = new JButton("Abbrechen");
            cancelButton.addActionListener(e -> frame.dispose());
            frame.add(cancelButton);

            JButton submitButton = new JButton("Absenden");

            accountTypeComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if(e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedAccountType = accountTypeComboBox.getSelectedItem().toString();

                        if (selectedAccountType.equals("Girokonto")) {

                            CurrentAccType = AccountType.Giro;
                            LimitLabel.setVisible(true);
                            dispoLimitField.setVisible(true);
                        } else if (selectedAccountType.equals("Festgeldkonto")) {

                            CurrentAccType = AccountType.SavingAccount;
                            LimitLabel.setVisible(false);
                            dispoLimitField.setVisible(false);
                        }
                    }
                }
            });

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {


                    BankAccount acc = null;
                    Random random = new Random();
                    String iban = "DE" + Integer.toString(random.nextInt(Integer.MAX_VALUE));
                    String secNum = Integer.toString(random.nextInt(999));
                    double limit = 0;
                    try{
                        limit = Double.parseDouble(dispoLimitField.getText());
                    }catch(Exception ex){
                        if(CurrentAccType == AccountType.Giro) {
                            JOptionPane.showMessageDialog(null, "Limit ist keine gültige Zahl!");
                            return;
                        }
                    }
                    switch (CurrentAccType){
                        case  Giro -> acc = new GiroAccount(iban, 0, DBProvider.getInstance().getCurrentCustomer(),kontonameField.getText(), limit);
                        case  SavingAccount -> acc = new SavingAccount(iban,0,DBProvider.getInstance().getCurrentCustomer(),kontonameField.getText());
                    }

                    if(acc == null){
                        JOptionPane.showMessageDialog(null, "Ungültiger Account typ!");
                        return;
                    }

                    DBProvider.getInstance().getCurrentCustomer().createAccount(acc);
                    DBProvider.getInstance().save();
                    ParentComponent.refreshAccountsCallback();
                    frame.setVisible(false);
                    frame.dispose();
                }

            });



            frame.add(submitButton);


            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}