
package com.pahana.edu.controller;

import com.pahana.edu.dao.*;
import com.pahana.edu.model.*;
import com.pahana.edu.db.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure server-side billing flow (no JS calculations required).
 * GET  /bill     -> show form
 * POST /bill     -> validate, calculate, persist (transaction), then redirect to /viewBill.jsp?billId=...
 */
@WebServlet(urlPatterns = {"/bill"})
public class BillController extends HttpServlet {

    private final CustomerDAO customerDAO = new CustomerDAOImpl();
    private final ItemDAO itemDAO = new ItemDAOImpl();
    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Prepare lists for the form
        req.setAttribute("customerList", customerDAO.getAllCustomers());
        req.setAttribute("itemList", itemDAO.getAllItems());
        req.getRequestDispatcher("/createBill_server.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Parse inputs (arrays for multi-rows). All calculations done server-side using DB prices.
        String customerId = req.getParameter("customerId");
        String[] itemIds = req.getParameterValues("itemId");
        String[] quantities = req.getParameterValues("quantity");
        String discountStr = req.getParameter("discountPct");
        BigDecimal discountPct = parsePercent(discountStr);

        if (customerId == null || customerId.isBlank()) {
            req.setAttribute("error", "Please select a customer.");
            doGet(req, resp);
            return;
        }
        if (itemIds == null || itemIds.length == 0) {
            req.setAttribute("error", "Please add at least one line item.");
            doGet(req, resp);
            return;
        }

        List<BillItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (int i = 0; i < itemIds.length; i++) {
            String id = itemIds[i];
            if (id == null || id.isBlank()) continue;
            int qty = parseIntSafe(quantities, i);
            if (qty <= 0) continue;

            Item item = itemDAO.getItemById(id);
            if (item == null) {
                req.setAttribute("error", "Unknown item: " + id);
                doGet(req, resp);
                return;
            }
            if (qty > item.getStockQuantity()) {
                req.setAttribute("error", "Insufficient stock for " + item.getItemName() + ". On hand: " + item.getStockQuantity());
                doGet(req, resp);
                return;
            }

            BigDecimal unitPrice = item.getUnitPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));
            subtotal = subtotal.add(lineTotal);

            BillItem bi = new BillItem();
            bi.setItemId(id);
            bi.setItemName(item.getItemName());
            bi.setQuantity(qty);
            bi.setUnitPrice(unitPrice);
            items.add(bi);
        }

        if (items.isEmpty()) {
            req.setAttribute("error", "Please add at least one valid item row.");
            doGet(req, resp);
            return;
        }

        // Discount (0..100)
        if (discountPct.compareTo(BigDecimal.ZERO) < 0 || discountPct.compareTo(new BigDecimal("100")) > 0) {
            req.setAttribute("error", "Discount must be between 0 and 100.");
            doGet(req, resp);
            return;
        }

        BigDecimal discountAmount = subtotal.multiply(discountPct).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        BigDecimal grandTotal = subtotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

        // Prepare Bill
        Bill bill = new Bill();
        bill.setBillDate(new java.sql.Date(System.currentTimeMillis()));
        bill.setCustomerId(customerId);
        bill.setItems(items);
        bill.setTotalAmount(grandTotal);

        // Get user from session (if available)
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object userObj = session.getAttribute("user");
            if (userObj instanceof User) {
                bill.setBilledByUserId(((User) userObj).getUserId());
            }
        }

        // Persist with transaction. BillDAOImpl manages inserts and stock updates.
        String newBillId;
        try {
            newBillId = billDAO.saveBill(bill); // implemented to insert bill header, items, and update stock atomically
        } catch (SQLException e) {
            throw new ServletException("Failed to save bill", e);
        }

        resp.sendRedirect(req.getContextPath() + "/viewBill?billId=" + newBillId);
    }

    private int parseIntSafe(String[] arr, int i) {
        try {
            return Integer.parseInt(arr[i]);
        } catch (Exception e) {
            return 0;
        }
    }

    private BigDecimal parsePercent(String s) {
        try {
            return new BigDecimal(s == null ? "0" : s.trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
