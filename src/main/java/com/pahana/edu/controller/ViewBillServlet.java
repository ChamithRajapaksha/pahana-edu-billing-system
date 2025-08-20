package com.pahana.edu.controller;

import com.pahana.edu.dao.BillDAO;
import com.pahana.edu.dao.BillDAOImpl;
import com.pahana.edu.model.Bill;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/viewBill")
public class ViewBillServlet extends HttpServlet {
    private BillDAO billDAO;

    public void init() {
        billDAO = new BillDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int billId = Integer.parseInt(request.getParameter("billId"));
            Bill bill = billDAO.getBillById(billId);
            request.setAttribute("bill", bill);
            request.getRequestDispatcher("viewBill.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminDashboard");
        }
    }
}