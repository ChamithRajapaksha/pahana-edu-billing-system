package com.pahana.edu.controller;

import com.pahana.edu.dao.ReportDAO;
import com.pahana.edu.dao.ReportDAOImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/adminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDAO reportDAO;

    public void init() {
        reportDAO = new ReportDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch all the summary data from the DAO
            long userCount = reportDAO.getTotalUserCount();
            long customerCount = reportDAO.getTotalCustomerCount();
            long itemsInStock = reportDAO.getTotalItemCount();
            BigDecimal todaySales = reportDAO.getTodaySalesTotal();

            // Set the data as request attributes for the JSP to access
            request.setAttribute("userCount", userCount);
            request.setAttribute("customerCount", customerCount);
            request.setAttribute("itemsInStock", itemsInStock);
            request.setAttribute("todaySales", todaySales);

            // Forward the request to the JSP page
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // In case of an error, redirect to the login page
            response.sendRedirect("login.jsp");
        }
    }
}
