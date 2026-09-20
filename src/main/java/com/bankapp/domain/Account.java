package com.bankapp.domain;

public class Account {
    private int id;
    private int pin;
    private int balance;


    public Account(int id, int pin, int balance) {
        this.balance = balance;
        this.id = id;
        this.pin = pin;
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

    boolean validatePIN(int pin) {
        //verify pin matches for login
        if(this.pin == pin) {
            return true;
        }
        return false;
    }
    
}