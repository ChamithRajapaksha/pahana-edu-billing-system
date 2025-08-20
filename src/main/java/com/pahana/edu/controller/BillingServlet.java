package com.pahana.edu.controller;

import com.pahana.edu.dao.*;
import com.pahana.edu.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/createBill")
public class BillingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;
    private BillDAO billDAO;

    public void init() {
        customerDAO = new CustomerDAOImpl();
        itemDAO = new ItemDAOImpl();
        billDAO = new BillDAOImpl();
    }

    /**
     * Handles GET requests. Fetches the list of all customers and items
     * and forwards them to the createBill.jsp page to populate the dropdowns.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Customer> customerList = customerDAO.getAllCustomers();
            List<Item> itemList = itemDAO.getAllItems();

            request.setAttribute("customerList", customerList);
            request.setAttribute("itemList", itemList);

            request.getRequestDispatcher("createBill.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally handle exceptions, e.g., by redirecting to an error page
        }
    }

    /**
     * Handles POST requests. Processes the submitted bill form, creates Bill and BillItem objects,
     * and saves them to the database using the BillDAO.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        try {
            // 1. Get basic bill information from the form
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));

            // 2. Get the arrays of item data submitted from the form
            String[] itemIds = request.getParameterValues("itemIds");
            String[] quantities = request.getParameterValues("quantities");
            String[] unitPrices = request.getParameterValues("unitPrices");

            // 3. Create a list of BillItem objects from the submitted data
            List<BillItem> billItems = new ArrayList<>();
            if (itemIds != null) {
                for (int i = 0; i < itemIds.length; i++) {
                    BillItem billItem = new BillItem();
                    billItem.setItemId(Integer.parseInt(itemIds[i]));
                    billItem.setQuantity(Integer.parseInt(quantities[i]));
                    billItem.setUnitPrice(new BigDecimal(unitPrices[i]));
                    billItems.add(billItem);
                }
            }
            
            // 4. Create the main Bill object
            Bill bill = new Bill();
            bill.setCustomerId(customerId);
            bill.setBilledByUserId(currentUser.getUserId());
            bill.setTotalAmount(totalAmount);
            bill.setBillDate(new Date(System.currentTimeMillis())); // Use current date for the bill
            bill.setItems(billItems);

            // 5. Save the bill using the DAO and get the new ID
            int newBillId = billDAO.saveBill(bill);

            // 6. Redirect to the new viewBill page to show the created bill and allow printing
            response.sendRedirect("viewBill?billId=" + newBillId);

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            // On error, set an error message and redirect back to the billing form
            session.setAttribute("errorMessage", "Error creating bill: " + e.getMessage());
            response.sendRedirect("createBill");
        }
    }
}
