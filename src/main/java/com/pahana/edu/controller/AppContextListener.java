package com.pahana.edu.controller;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.pahana.edu.db.DBConnection;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // This method is called when the application starts up.
        System.out.println(">>> Web Application Initialized. <<<");
    }

    /**
     * This method is called when the application is stopped or reloaded.
     * It's crucial for cleaning up resources to prevent memory leaks.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(">>> Web Application Shutting Down. Cleaning up resources. <<<");
        try {
            // 1. Close the singleton DB connection instance
            DBConnection.getInstance().closeConnection();
            
            // 2. Explicitly shut down the MySQL cleanup thread that causes the warning
            AbandonedConnectionCleanupThread.uncheckedShutdown();
            System.out.println(">>> MySQL cleanup thread shutdown successfully. <<<");
            
        } catch (SQLException e) {
            // Only SQLException needs to be caught here.
            e.printStackTrace();
        }
    }
}