package com.pahana.edu.controller;

import com.pahana.edu.dao.BillDAO;
import com.pahana.edu.dao.BillDAOImpl;
import com.pahana.edu.dao.CustomerDAO;
import com.pahana.edu.dao.CustomerDAOImpl;
import com.pahana.edu.model.Bill;
import com.pahana.edu.model.Customer;
import com.pahana.edu.service.ReceiptEmailService;
import com.pahana.edu.service.ReceiptEmailServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sendReceipt")
public class SendReceiptServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final BillDAO billDAO = new BillDAOImpl();
    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final ReceiptEmailService emailService = new ReceiptEmailServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String billId = request.getParameter("billId");
        HttpSession session = request.getSession();

        try {
            // 1. Fetch the full bill details from the database
            Bill bill = billDAO.getBillById(billId);
            if (bill == null) {
                throw new Exception("Bill with ID " + billId + " not found.");
            }

            // 2. Fetch the customer details to get their email address
            Customer customer = customerDAO.getCustomerById(bill.getCustomerId());
            if (customer == null) {
                throw new Exception("Customer for bill " + billId + " not found.");
            }
            if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
                throw new Exception("Email not sent: Customer does not have an email address on file.");
            }
            
            // 3. Call the email service to send the receipt
            emailService.sendBillReceipt(bill, customer);
            
            // 4. Set a success message
            session.setAttribute("flashMessage", "Receipt successfully sent to " + customer.getEmail());
            session.setAttribute("flashMessageType", "success");

        } catch (Exception e) {
            // If anything goes wrong, set an error message
            e.printStackTrace();
            session.setAttribute("flashMessage", "Error: " + e.getMessage());
            session.setAttribute("flashMessageType", "danger");
        }
        
        // 5. Redirect back to the same bill view page
        response.sendRedirect(request.getContextPath() + "/viewBill?billId=" + billId);
    }
}