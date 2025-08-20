package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pahana.edu.model.User;

import java.util.List;

class UserDAOImplTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
        // Note: For a real application, you'd use a separate, clean test database.
        // For this project, we'll use the main one.
    }

    @Test
    void testGetAllUsers() {
        System.out.println("Running test: testGetAllUsers");
        
        // Get the initial number of users
        int initialUserCount = userDAO.getAllUsers().size();
        
        // Add a new temporary user to test with
        User tempUser = new User();
        tempUser.setUsername("tempuser_for_test");
        tempUser.setPassword("password");
        tempUser.setFullName("Temporary Test User");
        tempUser.setRole("CASHIER");
        userDAO.addUser(tempUser);
        
        // Retrieve all users again
        List<User> allUsers = userDAO.getAllUsers();
        
        // Assert that the list is not empty and the count has increased by 1
        assertNotNull(allUsers, "The user list should not be null.");
        assertFalse(allUsers.isEmpty(), "The user list should not be empty.");
        assertEquals(initialUserCount + 1, allUsers.size(), "The user count should have incremented by one.");
        
        // Clean up: find and delete the temporary user
        User addedUser = userDAO.getUserByUsername("tempuser_for_test");
        if (addedUser != null) {
            userDAO.deleteUser(addedUser.getUserId());
        }
    }
}
