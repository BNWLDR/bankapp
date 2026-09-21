package com.bankapp.service;

import java.math.BigDecimal;

import com.bankapp.domain.*;

public interface BankService {

    //int logout(int id);
    int createAccount(String pin); // returns the new account id
    BigDecimal getBalance(int id);
    BigDecimal deposit(int id, BigDecimal amount); // returns the new balance
    BigDecimal withdraw(int id, BigDecimal amount); // returns the new balance
    void transfer(int id, int relId, BigDecimal amount);
    Transaction[] history(int id);
    int login(int id, String pin);           // returns the account id, or throws if invalid

    
}
