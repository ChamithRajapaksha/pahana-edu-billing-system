package com.pahana.edu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
        response.sendRedirect("login.jsp");
    }

    // It's good practice to have doPost call doGet for a simple logout servlet
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
