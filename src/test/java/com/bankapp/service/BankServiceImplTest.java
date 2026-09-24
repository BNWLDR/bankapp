package com.bankapp.service;

import com.bankapp.domain.Account;
import com.bankapp.domain.Transaction;
import com.bankapp.persistence.BankDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BankServiceImplTest {

    private FakeBankDAO dao;
    private BankServiceImpl service;

    @BeforeEach
    void setUp() {
        dao = new FakeBankDAO();
        dao.balances.put(1, new BigDecimal("100.00")); // account 1 starts with $100
        service = new BankServiceImpl(dao);
    }

    @Test
    void deposit_validAmount_returnsNewBalance() {
        BigDecimal result = service.deposit(1, new BigDecimal("25.50"));

        assertEquals(new BigDecimal("125.50"), result);
    }

    @Test
    void withdraw_moreThanBalance_throwsInsufficientFunds() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.withdraw(1, new BigDecimal("500.00")));

        assertEquals("Sorry, that account has insufficient funds.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), dao.balances.get(1)); // balance unchanged
    }

    /** In-memory stand-in for the real DB-backed DAO. */
    private static class FakeBankDAO implements BankDAO {
        final Map<Integer, BigDecimal> balances = new HashMap<>();

        public Account findAccount(int id) {
            BigDecimal bal = balances.get(id);
            return bal == null ? null : new Account(id, "1234", bal);
        }

        public BigDecimal deposit(int id, BigDecimal amount) {
            if (!balances.containsKey(id))
                return null;
            return balances.merge(id, amount, BigDecimal::add);
        }

        public BigDecimal withdraw(int id, BigDecimal amount) {
            BigDecimal bal = balances.get(id);
            if (bal == null || bal.compareTo(amount) < 0)
                return null; // mirrors real DAO contract
            return balances.merge(id, amount, BigDecimal::subtract);
        }

        public int addAccount(String pin) {
            throw new UnsupportedOperationException();
        }

        public boolean transfer(int id, int relId, BigDecimal amount) {
            throw new UnsupportedOperationException();
        }

        public Transaction[] history(int id) {
            throw new UnsupportedOperationException();
        }
    }
}
