package com.bankapp.service;

//import java.util.*;

import com.bankapp.persistence.*;
import com.bankapp.domain.*;

public class BankServiceImpl implements BankService{
    private final BankDAO bankDAO;

    public BankServiceImpl(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }


    // public int logout(int id) {       
    //     return 0;      
    // }

    public void createAccount(int pin){
        //check that account id is not already created
        int id = 0;
        if (pin != 0) { 
            id = bankDAO.addAccount(pin);
        }
        else {
            throw new IllegalArgumentException("PIN entered is incorrect");
        }

        if (id > 0) {
            System.out.println("Account created successfully! Your account ID is: " + id);
        }
        else {
            System.out.println("Sorry, we were unable to create your account at this time.");
        }
        
    }
    public void getBalance(int id){
        //check that account exists
        //return balance
        int res = bankDAO.getBalance(id);  
        if (res > -1) {
            System.out.printf("Account ID: %d | Current Balance: %.2f", id, res);
        }      
        else {
            System.out.println("Sorry, we were unable to check your account balance at this time.");
        }
    }
    public void updateBalance(String[] s){ //s[0] = id, s[1] = type, s[2] = amount, s[3] = ""
        
        int id = Integer.parseInt(s[0]);
        int amount = Integer.parseInt(s[2]);
        
        //verify Account exists
        if (bankDAO.getAccountId(id) < 0) {
            System.out.println("Sorry, that account does not exist.");
            return;
        }

        int balance = bankDAO.getBalance(id);
        
        if (s[1] == "withdraw") {
            //verify withdraw/deposit is valid
            if (balance >= amount) {
                //update balance and send to DAO to update table
                balance -= amount;
                int res = bankDAO.updateBalance(balance, balance);
                System.out.printf("%s succeeded! Account ID: %d | Current Balance: %.2f", s[1], id, res);
            }
            else {
                System.out.println("Sorry, that account has issuficient funds.");
            }
        }
        else if (s[1] == "deposit") {
            balance += amount;
            int res = bankDAO.updateBalance(id, amount);  
            System.out.printf("%s succeeded! Account ID: %d | Current Balance: %.2f", s[1], id, res);
        }            
    }

    public void transfer(String[] s){ //s[0] = id, s[1] = type, s[2] = amount, s[3] = related id
        
        //validate that account 1 has enough to transfer
        //update Account 1 and Account 2 and send to DAO to update table.
        
        int id = Integer.parseInt(s[0]);
        int amount = Integer.parseInt(s[2]);
        int rel = Integer.parseInt(s[3]);
        
        //validate account 1 and 2 exist
        if (bankDAO.getAccountId(id) < 0 || bankDAO.getAccountId(rel) < 0) {
            System.out.println("Sorry, unable to transfer between these accounts.");
            return;
        }
        int balance = bankDAO.getBalance(id);
        int relBalance = bankDAO.getBalance(rel);
        if (balance >= amount) {
                //update balance and send to DAO to update table
                balance -= amount;
                relBalance += amount;
                boolean res = bankDAO.transfer(id, balance, rel, relBalance);
                System.out.printf("%s succeeded! Account ID: %d | Current Balance: %.2f", s[1], id, res);
            }
            else {
                System.out.println("Sorry, that account has issuficient funds.");
            }

             
    }

    public void history(int id){
        //Validate that Account exists
        //return all Transactions with that ID
        //iterate through array of Accounts to print transactions
        bankDAO.history(id);        
    }

    public int login(int[] id){
        //Verify that Account exists
        //Return 1 if success, return 0 if invalid login
        bankDAO.login(id[0], id[1]);
            //return id[0];  
        
        return 0;
    }    
}
