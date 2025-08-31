package com.pahana.edu.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pahana.edu.dao.UserDAO;
import com.pahana.edu.dao.UserDAOImpl;
import com.pahana.edu.model.User;

@WebServlet("/manageUsers")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> userList = userDAO.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/manageUsers.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check for an 'action' parameter to determine what to do
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // Handle delete action
            int userId = Integer.parseInt(request.getParameter("userId"));
            userDAO.deleteUser(userId);
        } else {
            // Default action is to add a new user
            // **FIX**: Only get these parameters if we are NOT deleting
            String fullName = request.getParameter("fullName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole(role);

            userDAO.addUser(newUser);
        }

        // After the action is complete, redirect back to the user list
        response.sendRedirect(request.getContextPath() + "/manageUsers");
    }
}
