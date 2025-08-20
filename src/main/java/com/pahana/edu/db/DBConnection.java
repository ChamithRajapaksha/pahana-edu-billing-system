package com.pahana.edu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton Pattern to ensure only one DB connection instance is created.
public class DBConnection {

    private static DBConnection dbInstance;
    private Connection connection;

    // IMPORTANT: Make sure this URL, user, and password are correct for your MySQL setup.
    private static final String URL = "jdbc:mysql://localhost:3306/pahana_edu_db?useSSL=false";
    private static final String USER = "root"; // <-- CHANGE THIS
    private static final String PASSWORD = "1234"; // <-- CHANGE THIS

    private DBConnection() throws SQLException {
        try {
            // Register the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Attempt to establish the connection
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // --- DEBUGGING LINE ---
            // This message will print to the console if the connection is successful.
            System.out.println(">>> DATABASE CONNECTION SUCCESSFUL! <<<");
            
        } catch (ClassNotFoundException e) {
            // This error happens if the mysql-connector-java JAR is missing.
            System.err.println("!!! DATABASE ERROR: MySQL JDBC Driver not found. !!!");
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            // This error happens if the URL, username, or password is wrong.
            System.err.println("!!! DATABASE CONNECTION FAILED! Check URL, username, and password. !!!");
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException {
        // This ensures the connection is thread-safe and lazy-loaded.
        if (dbInstance == null || dbInstance.getConnection().isClosed()) {
            synchronized (DBConnection.class) {
                if (dbInstance == null || dbInstance.getConnection().isClosed()) {
                    dbInstance = new DBConnection();
                }
            }
        }
        return dbInstance;
    }
}
