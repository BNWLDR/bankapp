package com.bankapp.domain;

import java.math.BigDecimal;

public class Account {
    private int id;
    private String pin;
    private BigDecimal balance;


    public Account(int id, String pin, BigDecimal balance) {
        this.balance = balance;
        this.id = id;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return String.format( "Account ID: %d | Balance: %.2f", id, balance );
    }

    public int getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean validatePIN(String pin) {
        //verify pin matches for login
        return this.pin.equals(pin);
    }
    
}