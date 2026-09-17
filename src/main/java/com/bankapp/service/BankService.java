package com.bankapp.service;

public interface BankService {

    int logout(int id);
    void createAccount(int[] id);
    void getBalance(int id);
    void updateBalance(String[] s);
    void transfer(String[] s);
    void history(int id);
    int login(int[] res);
    void register(int[] res);

    
}
