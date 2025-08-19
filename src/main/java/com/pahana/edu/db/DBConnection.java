package com.pahana.edu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton Pattern 
public class DBConnection {

    private static DBConnection dbInstance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/pahana_edu_db?useSSL=false";
    private static final String USER = "root"; 
    private static final String PASSWORD = "1234"; 

    private DBConnection() throws SQLException {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
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