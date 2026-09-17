package com.bankapp.service;

import java.util.*;

import com.bankapp.persistence.*;

public class BankServiceImpl implements BankService{
    private final BankDAO bankDAO;

    public BankServiceImpl(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }


    public int logout(int id) {       
        bankDAO.logout(id);
        
        return 0;
    }
    public void createAccount(int[] id){
        //add check for negative amount of id[1]
        bankDAO.addAccount(id[0], id[1]);
    }
    public void getBalance(int id){
        //loop through all accounts under id and print balances
        bankDAO.getBalance(id);        
    }
    public void updateBalance(String[] s){ //s[0] = id, s[1] = type, s[2] = amount, s[3] = ""
        //verify id exists and that amount for withdraw is valid
        bankDAO.updateBalance(s[0], s[1], s[2]);        
    }
    public void deposit(String[] s){ //s[0] = id, s[1] = type, s[2] = amount, s[3] = ""
        bankDAO.updateBalance(s[0], s[1], s[2]);   
        
    }
    public void transfer(String[] s){ //s[0] = id, s[1] = type, s[2] = amount, s[3] = related id
        //validate accounts and balance for transmitting account
        bankDAO.transfer(s[0], s[1], s[2], s[3]);        
    }
    public void history(int id){
        bankDAO.history(id);        
    }
    public int login(int[] id){
        bankDAO.login(id[0], id[1]);
            //return id[0];  
        
        return 0;
    }
    public void register(int[] res){
        bankDAO.register(res[0], res[1]);
    }
    
}
