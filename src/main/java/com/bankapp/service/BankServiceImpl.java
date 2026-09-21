package com.bankapp.service;

import com.bankapp.persistence.*;
import com.bankapp.domain.*;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankServiceImpl implements BankService{
    private static final Logger logger = LoggerFactory.getLogger(BankServiceImpl.class);
    private final BankDAO bankDAO;

    public BankServiceImpl(BankDAO bankDAO) {
        this.bankDAO = bankDAO;
    }


    // public int logout(int id) {       
    //     return 0;      
    // }

    public int createAccount(String pin){
        //check that account id is not already created
        if (!pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }

        int id = bankDAO.addAccount(pin);

        if (id <= 0) {
            logger.info("Account {} created", id);
            System.out.println("Account created successfully! Your account ID is: " + id);
        }
        
        return id;
        
    }

    public BigDecimal getBalance(int id) {
        return findAccount(id).getBalance();
    }

    public BigDecimal deposit(int id, BigDecimal amount) {
        checkAmount(amount);
        BigDecimal balance = bankDAO.deposit(id, amount);
        if (balance == null) {
            throw new IllegalArgumentException("Sorry, that account does not exist.");
        }
        logger.info("Deposit of {} to account {} succeeded", amount, id);
        return balance;
    }

    public BigDecimal withdraw(int id, BigDecimal amount) {
        checkAmount(amount);
        getBalance(id); // throws if the account doesn't exist
        BigDecimal balance = bankDAO.withdraw(id, amount);
        if (balance == null) {
            logger.error("Withdrawal of {} from account {} refused: insufficient funds", amount, id);
            throw new IllegalArgumentException("Sorry, that account has insufficient funds.");
        }
        logger.info("Withdrawal of {} from account {} succeeded", amount, id);
        return balance;
    }

    public void transfer(int id, int relId, BigDecimal amount) {
        checkAmount(amount);
        if (id == relId) {
            throw new IllegalArgumentException("Sorry, you cannot transfer to the same account.");
        }
        getBalance(id); // both throw if the account doesn't exist
        getBalance(relId);
        if (!bankDAO.transfer(id, relId, amount)) {
            logger.error("Transfer of {} from account {} to {} refused: insufficient funds", amount, id, relId);
            throw new IllegalArgumentException("Sorry, that account has insufficient funds.");
        }
        logger.info("Transfer of {} from account {} to {} succeeded", amount, id, relId);
    }

    public Transaction[] history(int id) {
        findAccount(id); // throws if the account doesn't exist
        return bankDAO.history(id);
    }

    public int login(int id, String pin) {
        Account account = bankDAO.findAccount(id);
        if (account == null) {
            logger.error("Login attempted for non-existent account {}", id);
            throw new IllegalArgumentException("Invalid ID or PIN.");
        }
        if (!account.validatePIN(pin)) {
            logger.error("Incorrect PIN entered for account {}", id);
            throw new IllegalArgumentException("Invalid ID or PIN.");
        }
        logger.info("Account {} logged in", id);
        return account.getId();
    }

    private Account findAccount(int id) {
        Account account = bankDAO.findAccount(id);
        if (account == null) {
            throw new IllegalArgumentException("Sorry, that account does not exist.");
        }
        return account;
    }
    
    private void checkAmount(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Sorry, the amount must be greater than zero.");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Sorry, the amount can have at most 2 decimal places.");
        }
    }


}
