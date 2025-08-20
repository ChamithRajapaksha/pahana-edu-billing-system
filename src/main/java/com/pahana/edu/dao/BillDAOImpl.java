package com.pahana.edu.dao;

import com.pahana.edu.db.DBConnection;
import com.pahana.edu.model.Bill;
import com.pahana.edu.model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAOImpl implements BillDAO {

    /**
     * Saves a bill and its associated items to the database within a single transaction.
     * If any part of the save fails, the entire transaction is rolled back.
     * @param bill The Bill object to save.
     * @return The ID of the newly created bill.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public int saveBill(Bill bill) throws SQLException {
        Connection conn = null;
        int billId = -1; // Initialize billId to an invalid value

        try {
            conn = DBConnection.getInstance().getConnection();
            // Start a transaction by disabling auto-commit
            conn.setAutoCommit(false);

            // Step 1: Insert the main bill record into the 'bills' table
            String billSql = "INSERT INTO bills (customer_id, billed_by_user_id, total_amount, bill_date) VALUES (?, ?, ?, ?)";
            
            // Use Statement.RETURN_GENERATED_KEYS to get the auto-incremented bill_id
            try (PreparedStatement psBill = conn.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setInt(1, bill.getCustomerId());
                psBill.setInt(2, bill.getBilledByUserId());
                psBill.setBigDecimal(3, bill.getTotalAmount());
                psBill.setDate(4, bill.getBillDate());
                psBill.executeUpdate();

                // Retrieve the generated bill_id
                try (ResultSet generatedKeys = psBill.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        billId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating bill failed, no ID obtained.");
                    }
                }
            }

            // Step 2: Insert each item from the bill into the 'bill_items' table
            String itemSql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psItem = conn.prepareStatement(itemSql)) {
                for (BillItem item : bill.getItems()) {
                    psItem.setInt(1, billId); // Use the billId from the previous step
                    psItem.setInt(2, item.getItemId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getUnitPrice());
                    psItem.addBatch(); // Add the statement to a batch for efficient execution
                }
                psItem.executeBatch(); // Execute all item insert statements at once
            }

            // If all operations were successful, commit the transaction
            conn.commit();

        } catch (SQLException e) {
            // If any error occurs, roll back all changes made during the transaction
            if (conn != null) {
                try {
                    System.err.println("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            throw e; // Re-throw the exception so the servlet can handle it
        } finally {
            // Always restore the default auto-commit behavior
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return billId; // Return the new bill's ID
    }

    /**
     * Retrieves a single bill and all of its associated items from the database.
     * @param billId The ID of the bill to retrieve.
     * @return A Bill object complete with its items, or null if not found.
     * @throws SQLException if a database access error occurs.
     */
    @Override
    public Bill getBillById(int billId) throws SQLException {
        Bill bill = null;
        // SQL to get bill details and the customer's name using a JOIN
        String billSql = "SELECT b.*, c.first_name, c.last_name FROM bills b " +
                         "JOIN customers c ON b.customer_id = c.customer_id WHERE b.bill_id = ?";
        // SQL to get all items for a specific bill, including the item name from a JOIN
        String itemsSql = "SELECT bi.*, i.item_name FROM bill_items bi " +
                          "JOIN items i ON bi.item_id = i.item_id WHERE bi.bill_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            // Step 1: Get the main bill details
            try (PreparedStatement psBill = conn.prepareStatement(billSql)) {
                psBill.setInt(1, billId);
                try (ResultSet rsBill = psBill.executeQuery()) {
                    if (rsBill.next()) {
                        bill = new Bill();
                        bill.setBillId(rsBill.getInt("bill_id"));
                        bill.setCustomerId(rsBill.getInt("customer_id"));
                        bill.setTotalAmount(rsBill.getBigDecimal("total_amount"));
                        bill.setBillDate(rsBill.getDate("bill_date"));
                        bill.setCustomerName(rsBill.getString("first_name") + " " + rsBill.getString("last_name"));
                    }
                }
            }

            // Step 2: If the bill was found, get its line items
            if (bill != null) {
                List<BillItem> items = new ArrayList<>();
                try (PreparedStatement psItems = conn.prepareStatement(itemsSql)) {
                    psItems.setInt(1, billId);
                    try (ResultSet rsItems = psItems.executeQuery()) {
                        while (rsItems.next()) {
                            BillItem item = new BillItem();
                            item.setItemName(rsItems.getString("item_name"));
                            item.setQuantity(rsItems.getInt("quantity"));
                            item.setUnitPrice(rsItems.getBigDecimal("unit_price"));
                            items.add(item);
                        }
                    }
                }
                bill.setItems(items);
            }
        }
        return bill;
    }
}
