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
    private static final String USER = "root"; // <-- CHANGE THIS IF NEEDED
    private static final String PASSWORD = "1234"; // <-- CHANGE THIS IF NEEDED

    private DBConnection() throws SQLException {
        try {
            // Register the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Attempt to establish the connection
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
            System.out.println(">>> DATABASE CONNECTION SUCCESSFUL! <<<");
            
        } catch (ClassNotFoundException e) {
            System.err.println("!!! DATABASE ERROR: MySQL JDBC Driver not found. !!!");
            e.printStackTrace();
            throw new SQLException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
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
    
    /**
     * NEW METHOD: Closes the database connection.
     * This is called by the AppContextListener when the application shuts down
     * to prevent memory leaks.
     * @throws SQLException if a database access error occurs.
     */
    public void closeConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
            System.out.println(">>> DATABASE CONNECTION CLOSED! <<<");
        }
    }
}
