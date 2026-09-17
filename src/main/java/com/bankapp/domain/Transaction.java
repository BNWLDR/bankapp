package com.bankapp.domain;

public class Transaction {
    int transactionID;
    int accountID;
    String transactionType;
    int amount;
    int relatedID;
    String timestamp;

    public Transaction(int transactionID, int accountID, String transactionType, int amount, int relatedID, String timestamp) {
        this.transactionID = transactionID;
        this.accountID = accountID;
        this.transactionType = transactionType;
        this.amount = amount;
        this.relatedID = relatedID;
        this.timestamp = timestamp;
    }

    @Override 
    public String toString() { //toString prints account information
        return String.format("Transaction toString() not implemented yet.");
    }

    
}
