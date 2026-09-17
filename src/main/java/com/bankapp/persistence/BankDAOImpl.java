package com.bankapp.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bankapp.domain.*;

public class BankDAOImpl implements BankDAO {

    //     CREATE TABLE account (
    //     account_id SERIAL PRIMARY KEY,
    //     pin VARCHAR(255) NOT NULL,
    //     balance NUMERIC(12, 2) NOT NULL DEFAULT 0.00
    //     );

    // CREATE TABLE transaction (
    //     transaction_id SERIAL PRIMARY KEY,
    //     account_id INTEGER NOT NULL REFERENCES account(account_id),
    //     type VARCHAR(20) NOT NULL,
    //     amount NUMERIC(12, 2) NOT NULL,
    //     related_account_id INTEGER REFERENCES account(account_id),
    //     timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    //     );
    
    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO account (pin, balance) VALUES (?, ?) RETURNING account_id";
    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO transaction (id, name, major, gpa) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_SQL = "SELECT id, name, major, gpa FROM account ORDER BY id";
    private static final String SELECT_TRASACTION_SQL = "SELECT id, name, major, gpa FROM transaction ORDER BY id";
    private static final String UPDATE_BALANCE_SQL = "UPDATE account SET balance = ? WHERE id = ?";
    //private static final String DELETE_ACCOUNT_SQL = "DELETE FROM account WHERE id = ?";




        public boolean logout(int id){

            return false;
        }

        public void addAccount(int pin) {
            int res;
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL)) {
                statement.setInt(1, pin);
                statement.setInt(2, 0);
                res = statement.executeUpdate();
            } catch (SQLException e) {
                throw databaseError("Could not add student", e);
            }
            System.out.println("Account created successfully! Your id is: " + res);

        }

        public void getBalance(int id){

        }

        public void updateBalance(String id, String type, String amount){

        }

        public void transfer(String id, String type, String amount, String relId){

        }

        public void history(int id){

        }

        public boolean login(int id, int pin){

            return false;
        }

        public boolean register(int id, int pin){

            return false;
        }

        public boolean verifyId(int id){

            return false;
        }

        public boolean verifyBalance(int amount){

            return false;
        }

        public boolean verifyPIN(int pin){

            return false;
        }

        private IllegalStateException databaseError(String message, SQLException cause) {
            return new IllegalStateException(message, cause);
        }
    
}
