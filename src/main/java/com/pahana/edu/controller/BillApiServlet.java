package com.pahana.edu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pahana.edu.dao.*;
import com.pahana.edu.dto.BillApiRequest;
import com.pahana.edu.dto.BillApiResponse;
import com.pahana.edu.model.*;
import com.pahana.edu.service.ReceiptEmailService;
import com.pahana.edu.service.ReceiptEmailServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the creation of bills via a dedicated REST API endpoint.
 * Accepts JSON, uses String-based sequential IDs, and sends email receipts.
 */
@WebServlet("/api/v1/bills")
public class BillApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DAOs and Services
    private BillDAO billDAO;
    private ItemDAO itemDAO;
    private CustomerDAO customerDAO;
    private ReceiptEmailService emailService; // FIX: Uncommented email service

    // Utilities
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        billDAO = new BillDAOImpl();
        itemDAO = new ItemDAOImpl();
        customerDAO = new CustomerDAOImpl();
        emailService = new ReceiptEmailServiceImpl(); // FIX: Uncommented initialization
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"A valid user session is required.\"}");
            return;
        }
        User currentUser = (User) session.getAttribute("user");

        try {
            // 1. Parse JSON request from the client into our DTO
            String jsonPayload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            BillApiRequest billRequest = objectMapper.readValue(jsonPayload, BillApiRequest.class);

            // 2. Validate the request data
            validateRequest(billRequest);

            // 3. Process the request and build the Bill object
            List<BillItem> billItems = new ArrayList<>();
            BigDecimal subTotal = BigDecimal.ZERO;

            for (BillApiRequest.BillLineItem line : billRequest.getLines()) {
                Item itemFromDB = itemDAO.getItemById(line.getItemId());
                if (itemFromDB.getStockQuantity() < line.getQty()) {
                    throw new IllegalStateException("Insufficient stock for item: " + itemFromDB.getItemName());
                }
                BillItem billItem = new BillItem();
                billItem.setItemId(line.getItemId());
                billItem.setQuantity(line.getQty());
                billItem.setUnitPrice(itemFromDB.getUnitPrice());
                // Note: itemName will be populated later for the email
                billItems.add(billItem);
                subTotal = subTotal.add(itemFromDB.getUnitPrice().multiply(BigDecimal.valueOf(line.getQty())));
            }

            BigDecimal discountAmount = subTotal.multiply(billRequest.getDiscountPct().divide(new BigDecimal("100")));
            BigDecimal grandTotal = subTotal.subtract(discountAmount);

            String newBillId = billDAO.generateNewBillId();

            Bill billToSave = new Bill();
            billToSave.setBillId(newBillId);

            billToSave.setCustomerId(billRequest.getCustomerId());
            billToSave.setBilledByUserId(currentUser.getUserId());
            billToSave.setTotalAmount(grandTotal.setScale(2, RoundingMode.HALF_UP));
            billToSave.setBillDate(new Date(System.currentTimeMillis()));
            billToSave.setItems(billItems);

            // 4. Save the bill atomically via the DAO
            billDAO.saveBill(billToSave);

            // 5. Send the email receipt
            Customer customer = customerDAO.getCustomerById(billToSave.getCustomerId());
            if (customer != null && customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                // Fetch the full bill details (with item names) to ensure the email is complete
                Bill fullBillDetails = billDAO.getBillById(newBillId);
                if (fullBillDetails != null) {
                    emailService.sendBillReceipt(fullBillDetails, customer);
                }
            }

            // 6. Send a success response back to the client
            BillApiResponse successResponse = new BillApiResponse(newBillId, grandTotal.setScale(2, RoundingMode.HALF_UP));
            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getOutputStream(), successResponse);

        } catch (IllegalArgumentException | IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(String.format("{\"error\": \"%s\"}", e.getMessage()));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error during bill creation.\"}");
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An unexpected server error occurred.\"}");
            e.printStackTrace();
        }
    }

    /**
     * Performs server-side validation using String IDs.
     */
    private void validateRequest(BillApiRequest request) throws IllegalArgumentException {
        if (customerDAO.getCustomerById(request.getCustomerId()) == null) {
            throw new IllegalArgumentException("Customer with ID " + request.getCustomerId() + " not found.");
        }
        if (request.getLines() == null || request.getLines().isEmpty()) {
            throw new IllegalArgumentException("A bill must contain at least one item.");
        }
        if (request.getDiscountPct() == null || request.getDiscountPct().compareTo(BigDecimal.ZERO) < 0 || request.getDiscountPct().compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100.");
        }
        for (BillApiRequest.BillLineItem line : request.getLines()) {
            if (line.getQty() <= 0) {
                throw new IllegalArgumentException("Item quantity must be a positive number.");
            }
        }
    }
}
