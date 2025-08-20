package com.pahana.edu.service;

import com.pahana.edu.dao.UserDAO;
import com.pahana.edu.dao.UserDAOImpl;
import com.pahana.edu.model.User;
import com.pahana.edu.util.PasswordUtil;

public class AuthService {

    private UserDAO userDAO = new UserDAOImpl();

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        
        if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user; // Login successful
        }
        
        return null; // Login failed
    }
}