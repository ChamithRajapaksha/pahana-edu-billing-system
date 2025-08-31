package com.pahana.edu.controller;

import com.pahana.edu.dao.ReportDAO;
import com.pahana.edu.dao.ReportDAOImpl;
import com.pahana.edu.model.Bill;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReportDAO reportDAO;

    public void init() {
        reportDAO = new ReportDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Bill> recentBills = reportDAO.getRecentBills(10);
            Map<String, Integer> topSellingItems = reportDAO.getTopSellingItems(5);

            request.setAttribute("recentBills", recentBills);
            request.setAttribute("topSellingItems", topSellingItems);

            request.getRequestDispatcher("/viewReports.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/adminDashboard"); // Redirect to a safe page on error
        }
    }
}
