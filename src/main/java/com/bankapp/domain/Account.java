package com.bankapp.domain;

public class Account {
    int id;
    int balance;


    public Account(int id, int balance) {
        this.balance = balance;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format( "Account ID: %d | Balance: %d", id, balance );
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }
    
}