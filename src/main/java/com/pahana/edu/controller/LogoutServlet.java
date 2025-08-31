package com.pahana.edu.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the current session associated with the request
        HttpSession session = request.getSession(false); // 'false' means do not create a new session if one doesn't exist

        if (session != null) {
            // Invalidate the session, which removes all attributes (like the "user" object)
            session.invalidate();
        }

        // Redirect the user back to the login page
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    // It's good practice to have doPost call doGet for a simple logout servlet
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
