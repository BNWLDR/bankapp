package com.bankapp.persistence;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.bankapp.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.bankapp.domain.*;

public class BankDAOImpl implements BankDAO {

    // table structures for reference:
    //     CREATE TABLE account (
    //     account_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    //     pin VARCHAR(255) NOT NULL,
    //     balance NUMERIC(12, 2) NOT NULL DEFAULT 0.00
    //     );

    // CREATE TABLE transaction (
    //     transaction_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    //     account_id INTEGER NOT NULL REFERENCES account(account_id),
    //     type VARCHAR(20) NOT NULL,
    //     amount NUMERIC(12, 2) NOT NULL,
    //     related_account_id INTEGER REFERENCES account(account_id),
    //     timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    //     );

    private static final Logger logger = LoggerFactory.getLogger(BankDAOImpl.class);
    
    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO account (pin, balance) VALUES (?, ?) RETURNING account_id";
    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO transaction (account_id, type, amount, related_account_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_SQL = "SELECT account_id, pin, balance FROM account WHERE account_id = ?";
    private static final String SELECT_TRASACTION_SQL = "SELECT transaction_id, account_id, type, amount, related_account_id, timestamp " +
                                                        "FROM transaction WHERE account_id = ? ORDER BY transaction_id DESC LIMIT 10";
    private static final String DEPOSIT_SQL = "UPDATE account SET balance = balance + ? WHERE account_id = ? RETURNING balance";
    private static final String WITHDRAW_SQL = "UPDATE account SET balance = balance - ? WHERE account_id = ? AND balance >= ? RETURNING balance";

    //private static final String DELETE_ACCOUNT_SQL = "DELETE FROM account WHERE id = ?";



        public int addAccount(String pin) {
            ResultSet res;
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL)) {
                statement.setString(1, pin);
                statement.setInt(2, 0);
                res = statement.executeQuery();
                if(res.next()){
                    return res.getInt("account_id");
                }
                
            } catch (SQLException e) {
                throw databaseError("Error creating account", e);
            }
            return 0;
        }

        public Account findAccount(int id) {
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                    PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_SQL)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return new Account(rs.getInt("account_id"), rs.getString("pin"), rs.getBigDecimal("balance"));
                    }
                }
            } catch (SQLException e) {
                throw databaseError("Error retrieving account", e);
            }
            return null;
        }                

        public Transaction[] history(int id){
        List<Transaction> res = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_TRASACTION_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    res.add(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getString("type"),
                        rs.getBigDecimal("amount"),
                        rs.getInt("related_account_id"),   // returns 0 when the column is NULL
                        rs.getTimestamp("timestamp").toString()));
                }
            }
        } catch (SQLException e) {
            throw databaseError("Error retrieving transaction history", e);
        }
        return res.toArray(new Transaction[0]);
    }

        

        public BigDecimal deposit(int id, BigDecimal amount) {
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {
                connection.setAutoCommit(false);
                try {
                    BigDecimal balance = depositInto(connection, id, amount);
                    if (balance == null) {
                        connection.rollback();
                        return null;
                    }
                    insertTransaction(connection, id, amount, "deposit", 0);
                    connection.commit();
                    return balance;
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                throw databaseError("Error making deposit", e);
            }
        }

        public BigDecimal withdraw(int id, BigDecimal amount) {
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {
                connection.setAutoCommit(false);
                try {
                    BigDecimal balance = withdrawFrom(connection, id, amount);
                    if (balance == null) {
                        connection.rollback();
                        return null;
                    }
                    insertTransaction(connection, id, amount, "withdraw", 0);
                    connection.commit();
                    return balance;
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                throw databaseError("Error making withdrawal", e);
            }
        }

        public boolean transfer(int id, int relId, BigDecimal amount) {
            // id -> relId
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {
                connection.setAutoCommit(false);
                try {
                    if (withdrawFrom(connection, id, amount) == null || depositInto(connection, relId, amount) == null) {
                        connection.rollback();
                        return false;
                    }
                    insertTransaction(connection, id, amount, "transfer_out", relId);
                    insertTransaction(connection, relId, amount, "transfer_in", id);
                    connection.commit();
                    return true;
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            } catch (SQLException e) {
                throw databaseError("Error transferring funds", e);
            }
        }


        private BigDecimal depositInto(Connection connection, int id, BigDecimal amount) throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement(DEPOSIT_SQL)) {
                statement.setBigDecimal(1, amount);
                statement.setInt(2, id);
                try (ResultSet rs = statement.executeQuery()) {
                    return rs.next() ? rs.getBigDecimal("balance") : null;
                }
            }
        }

        private BigDecimal withdrawFrom(Connection connection, int id, BigDecimal amount) throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement(WITHDRAW_SQL)) {
                statement.setBigDecimal(1, amount);
                statement.setInt(2, id);
                statement.setBigDecimal(3, amount);
                try (ResultSet rs = statement.executeQuery()) {
                    return rs.next() ? rs.getBigDecimal("balance") : null;
                }
            }
        }

        private void insertTransaction(Connection connection, int id, 
                BigDecimal amount, String type, int relId)
                throws SQLException {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION_SQL)) {
                statement.setInt(1, id);
                statement.setString(2, type);
                statement.setBigDecimal(3, amount);
                if (relId == 0) {
                    statement.setNull(4, java.sql.Types.INTEGER); // no related account
                } else {
                    statement.setInt(4, relId);
                }
                statement.executeUpdate();
            }
        }

        private DatabaseException databaseError(String message, SQLException cause) {
            logger.error(message, cause);
            return new DatabaseException(message, cause);
        }
    
}
