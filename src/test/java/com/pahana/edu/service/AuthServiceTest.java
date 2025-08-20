package com.pahana.edu.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.pahana.edu.dao.UserDAO;
import com.pahana.edu.dao.UserDAOImpl;
import com.pahana.edu.model.User;
import com.pahana.edu.util.PasswordUtil;

class AuthServiceTest {

    private static AuthService authService;
    
    // This method runs once before any tests start.
    // We use it to set up a test user in the database.
    @BeforeAll
    static void setup() {
        authService = new AuthService();
        UserDAO userDAO = new UserDAOImpl();
        
        // Ensure a consistent test user exists
        String testUsername = "testcashier";
        String testPassword = "password123";
        
        // Delete the user if it already exists to ensure a clean state
        User existingUser = userDAO.getUserByUsername(testUsername);
        if (existingUser != null) {
            userDAO.deleteUser(existingUser.getUserId());
        }
        
        // Create a fresh test user
        User testUser = new User();
        testUser.setUsername(testUsername);
        testUser.setPassword(testPassword); // The DAO will hash this
        testUser.setFullName("Test Cashier");
        testUser.setRole("CASHIER");
        userDAO.addUser(testUser);
    }

    @Test
    void testLoginWithValidCredentials() {
        System.out.println("Running test: testLoginWithValidCredentials");
        // Attempt to log in with the correct credentials we set up
        User user = authService.login("testcashier", "password123");
        
        // Assert that the login was successful (user is not null)
        assertNotNull(user, "User should not be null for valid credentials.");
        
        // Assert that the correct user was returned
        assertEquals("testcashier", user.getUsername(), "Username should match.");
        assertEquals("CASHIER", user.getRole(), "User role should be CASHIER.");
    }

    @Test
    void testLoginWithInvalidCredentials() {
        System.out.println("Running test: testLoginWithInvalidCredentials");
        // Attempt to log in with a wrong password
        User user = authService.login("testcashier", "wrongpassword");
        
        // Assert that the login failed (user is null)
        assertNull(user, "User should be null for invalid credentials.");
    }
}
