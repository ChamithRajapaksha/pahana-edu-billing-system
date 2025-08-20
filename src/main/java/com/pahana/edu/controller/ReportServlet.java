package com.pahana.edu.controller;

import com.pahana.edu.dao.ReportDAO;
import com.pahana.edu.dao.ReportDAOImpl;
import com.pahana.edu.model.Bill;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDAO reportDAO;

    public void init() {
        // Initialize the DAO when the servlet is first loaded
        reportDAO = new ReportDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Fetch the data for the reports using the DAO
            List<Bill> recentBills = reportDAO.getRecentBills(10); // Get the last 10 bills
            Map<String, Integer> topSellingItems = reportDAO.getTopSellingItems(5); // Get the top 5 selling items

            // Set the retrieved data as request attributes so the JSP can access it
            request.setAttribute("recentBills", recentBills);
            request.setAttribute("topSellingItems", topSellingItems);

            // Forward the request and the data to the JSP page for display
            request.getRequestDispatcher("viewReports.jsp").forward(request, response);

        } catch (Exception e) {
            // Print any errors to the console and redirect to the dashboard
            e.printStackTrace();
            response.sendRedirect("adminDashboard");
        }
    }
}
