package com.pahana.edu.controller;

// Import all necessary DAO interfaces and the User model
import com.pahana.edu.dao.BillDAO;
import com.pahana.edu.dao.BillDAOImpl;
import com.pahana.edu.dao.CustomerDAO;
import com.pahana.edu.dao.CustomerDAOImpl;
import com.pahana.edu.dao.ItemDAO;
import com.pahana.edu.dao.ItemDAOImpl;
import com.pahana.edu.dao.UserDAO;
import com.pahana.edu.dao.UserDAOImpl;
import com.pahana.edu.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Instantiate all the DAOs you'll need to fetch data
    private final UserDAO userDAO = new UserDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Security check to ensure an admin is logged in
        if (session == null || session.getAttribute("user") == null || !((User)session.getAttribute("user")).getRole().equals("ADMIN")) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // --- Live Data Fetching Logic ---
            // Call the DAO methods to get real data from the database
            int userCount = userDAO.countAllUsers();
            int customerCount = customerDAO.countAllCustomers(); // Assuming you created this method
            int itemsInStock = itemDAO.sumTotalStock();
            BigDecimal todaySales = billDAO.getTodaysSales();

            // --- Set Attributes for the JSP ---
            // The JSP will use these attributes to display the live data
            request.setAttribute("userCount", userCount);
            request.setAttribute("customerCount", customerCount);
            request.setAttribute("itemsInStock", itemsInStock);
            request.setAttribute("todaySales", todaySales);

        } catch (SQLException e) {
            // Handle any database errors gracefully
            e.printStackTrace(); // Log the full error to the console for debugging
            request.setAttribute("errorMessage", "Could not load dashboard data due to a database error.");
        }

        // --- Forward to the JSP View ---
        request.getRequestDispatcher("/adminDashboard.jsp").forward(request, response);
    }
}