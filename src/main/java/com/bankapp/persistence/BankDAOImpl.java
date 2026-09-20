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
    
    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO account (pin, balance) VALUES (?, ?) RETURNING account_id";
    private static final String INSERT_TRANSACTION_SQL = "INSERT INTO transaction (account_id, type, amount, related_account_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_SQL = "SELECT account_id, pin, balance FROM account WHERE account_id = ? RETURNING account_id, pin, balance";
    private static final String SELECT_TRASACTION_SQL = "SELECT id, name, major, gpa FROM transaction ORDER BY id";
    private static final String UPDATE_BALANCE_SQL = "UPDATE account SET balance = ? WHERE id = ?";
    //private static final String DELETE_ACCOUNT_SQL = "DELETE FROM account WHERE id = ?";



        public int addAccount(int pin) {
            ResultSet res;
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT_SQL)) {
                statement.setInt(1, pin);
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

        public int getBalance(int id){
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_SQL)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                if(rs.next()){
                    return rs.getInt("balance");
                }
            } catch (SQLException e) {
                throw databaseError("Error retrieving balance", e);
            }
            return -1;
        }

        public int updateBalance(int id, int amount){
            //check that account 
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_BALANCE_SQL)) {
                statement.setInt(1, amount);
                statement.executeQuery();
                return 1;
                
            } catch (SQLException e) {
                throw databaseError("Error retrieving balance", e);
            }
            

        }

        public boolean transfer(int id, int balance, int relId, int relBalance){
            // id -> relId
            // balance -> amount -> relBalance
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection()) {

                connection.setAutoCommit(false);
                try (
                    PreparedStatement wst = connection.prepareStatement(UPDATE_BALANCE_SQL);
                PreparedStatement dst = connection.prepareStatement(UPDATE_BALANCE_SQL)) {

                wst.setInt(1, balance);
                wst.setInt(2, id);
                int wres = wst.executeUpdate();

                dst.setInt(1, relBalance);
                dst.setInt(2, relId);
                int dres = dst.executeUpdate();

                if (wres == 1 && dres == 1) {
                    connection.commit();
                    return true;
                }
                else {
                    connection.rollback();
                    return false;
                }

                
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rbEx) {
                    e.addSuppressed(rbEx);

                }
                throw databaseError("Error retrieving balance", e);
            }
        } catch (SQLException e) {
            throw databaseError("Error retrieving balance", e);
        }
        }

        public Transaction[] history(int id){
            Transaction[] res = new Transaction[1]; //placeholder initilization

            return res;

        }

        public int[] login(int id, int pin){
            //Store login/logout in log.txt if needed
            int[] res = new int[2];
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_SQL)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    res[0] = rs.getInt("account_id");
                    res[1] = rs.getInt("pin");
                }
            } catch (SQLException e) {
                throw databaseError("Error retrieving balance", e);
            }
            return res;
        }


        public int getAccountId(int id){
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_SQL)) {
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    return rs.getInt("account_id");
                }
            } catch (SQLException e) {
                throw databaseError("Error retrieving account id", e);
            }
            return -1;
        }

        public int addTransaction(int id, int amount, String type, int relId) {

            //(account_id, type, amount, related_account_id)
            try (Connection connection = ConnectionFactory.getConnectionFactory().getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION_SQL)) {
                statement.setInt(1, id);
                statement.setString(2, type);
                statement.setInt(3, amount);
                statement.setInt(4, relId);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    return rs.getInt("account_id");
                }
            } catch (SQLException e) {
                throw databaseError("Error retrieving account id", e);
            }

            return -1;

        }

        private IllegalStateException databaseError(String message, SQLException cause) {
            return new IllegalStateException(message, cause);
        }
    
}
