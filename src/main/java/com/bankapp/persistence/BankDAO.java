package com.bankapp.persistence;

public interface BankDAO {
    boolean logout(int id);

    boolean addAccount(int id, int pin);

    void getBalance(int id);

    void updateBalance(String id, String type, String amount);

    void transfer(String id, String type, String amount, String relId);

    void history(int id);

    boolean login(int id, int pin);

    boolean register(int id, int pin);

    boolean verifyId(int id);

    boolean verifyBalance(int amount);

    boolean verifyPIN(int pin);
    
}
