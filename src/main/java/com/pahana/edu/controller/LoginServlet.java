package com.pahana.edu.controller;

import com.pahana.edu.model.User;
import com.pahana.edu.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward any GET requests to the login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User user = authService.login(username, password);
        
        if (user != null) {
            // Successful login: Create a session and store the user object
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // --- THIS IS THE UPDATED LOGIC ---
            // Redirect the user based on their role
            if ("ADMIN".equals(user.getRole())) {
                // Admins go to the dynamic admin dashboard servlet
                response.sendRedirect("adminDashboard"); 
            } else if ("CASHIER".equals(user.getRole())) {
                // Cashiers go to their new, dedicated dashboard page
                response.sendRedirect("cashierDashboard.jsp");
            } else {
                // If the role is unknown, send them back to the login page
                response.sendRedirect("login.jsp");
            }
        } else {
            // Failed login: Set an error message and forward back to the login page
            request.setAttribute("errorMessage", "Invalid username or password. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
