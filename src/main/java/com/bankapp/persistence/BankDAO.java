package com.bankapp.persistence;
import com.bankapp.domain.*;

public interface BankDAO {

    int addAccount(int pin);

    int getBalance(int id);

    int updateBalance(int id, int amount);

    boolean transfer(int id, int balance, int relId, int relBalance);

    Transaction[] history(int id);

    int[] login(int id, int pin);

    int getAccountId(int id);
    
}
