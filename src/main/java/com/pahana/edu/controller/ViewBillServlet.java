package com.pahana.edu.controller;

import com.pahana.edu.dao.BillDAO;
import com.pahana.edu.dao.BillDAOImpl;
import com.pahana.edu.model.Bill;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// Map this servlet to the URL that will display a bill
@WebServlet("/viewBill")
public class ViewBillServlet extends HttpServlet {

    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Get the billId from the URL parameter
        String billId = req.getParameter("billId");

        if (billId == null || billId.trim().isEmpty()) {
            // Handle case where no ID is provided
            req.setAttribute("error", "No Bill ID provided.");
            req.getRequestDispatcher("/viewBill.jsp").forward(req, resp);
            return;
        }

        try {
            // 2. Fetch the Bill object from the database using the DAO
            Bill bill = billDAO.getBillById(billId);
            
            // 3. Set the fetched bill object as a request attribute
            // This makes it available to the JSP as ${bill}
            req.setAttribute("bill", bill);

        } catch (SQLException e) {
            // Log the error and prepare an error message for the user
            e.printStackTrace();
            req.setAttribute("error", "Error retrieving bill details from the database.");
        }
        
        // 4. Forward the request (with the attached 'bill' object) to the JSP for rendering
        req.getRequestDispatcher("/viewBill.jsp").forward(req, resp);
    }
}