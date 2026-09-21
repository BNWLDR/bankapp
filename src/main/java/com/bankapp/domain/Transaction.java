package com.bankapp.domain;

import java.math.BigDecimal;

public class Transaction {
    int transactionID;
    int accountID;
    String transactionType;
    BigDecimal amount;
    int relatedID;
    String timestamp;

    public Transaction(int transactionID, int accountID, String transactionType, BigDecimal amount, int relatedID, String timestamp) {
        this.transactionID = transactionID;
        this.accountID = accountID;
        this.transactionType = transactionType;
        this.amount = amount;
        this.relatedID = relatedID;
        this.timestamp = timestamp;
    }

    @Override 
    public String toString() { //toString prints account information
        return transactionType + ": " + amount + " | Account: " + accountID + " | Related: " + relatedID + " | " + timestamp;
    }

    
}
