package com.pahana.edu.dao;

import com.pahana.edu.model.User;
import java.util.List;

public interface UserDAO {
    User getUserByUsername(String username);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);
    List<User> getAllUsers();
    User getUserById(int userId);
}