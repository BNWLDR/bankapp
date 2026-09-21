package com.bankapp.persistence;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import com.bankapp.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionFactory {

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private Properties props = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);

    

    private ConnectionFactory() {
    }

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public Connection getConnection() {
        if (props.isEmpty()) {
            loadProperties();
        }
        try {
            return DriverManager.getConnection(
                    props.getProperty("DB_URL"),
                    props.getProperty("DB_USER"),
                    props.getProperty("DB_PASSWORD"));
        } catch (SQLException e) {
            logger.error("Database connection lost or unavailable", e);
            throw new DatabaseException("Could not connect to the database", e);
        }
    }

    private void loadProperties() {
        try (InputStream in = ConnectionFactory.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                logger.error("Database connection lost or unavailable");
                throw new DatabaseException(
                    "db.properties not found on the classpath (expected src/main/resources/db.properties)");
            }
            props.load(in);
        } catch (IOException e) {
            logger.error("Database connection lost or unavailable", e);
            throw new DatabaseException("Could not read db.properties", e);
        }
    }
}
