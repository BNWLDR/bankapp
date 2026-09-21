package com.bankapp.persistence;
import java.math.BigDecimal;

import com.bankapp.domain.*;

public interface BankDAO {

    int addAccount(String pin);

    BigDecimal deposit(int id, BigDecimal amount);

    BigDecimal withdraw(int id, BigDecimal amount);

    boolean transfer(int id, int relId, BigDecimal amount);

    Transaction[] history(int id);

    Account findAccount(int id);

    
    
}
