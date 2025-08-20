package com.pahana.edu.dao;

import com.pahana.edu.model.Bill;
import java.sql.SQLException;

/**
 * Data Access Object interface for Bill-related database operations.
 * This interface defines the standard operations to be performed on Bill objects.
 */
public interface BillDAO {

    /**
     * Saves a complete bill, including all its items, to the database.
     * This operation should be transactional.
     *
     * @param bill The Bill object containing all necessary information to be saved.
     * @return The auto-generated ID of the newly saved bill.
     * @throws SQLException if a database access error occurs.
     */
    int saveBill(Bill bill) throws SQLException;

    /**
     * Retrieves a single bill and all of its associated items from the database.
     *
     * @param billId The ID of the bill to retrieve.
     * @return A Bill object complete with its items, or null if the bill is not found.
     * @throws SQLException if a database access error occurs.
     */
    Bill getBillById(int billId) throws SQLException;
}