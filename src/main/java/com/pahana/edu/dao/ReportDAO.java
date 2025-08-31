package com.pahana.edu.dao;

import com.pahana.edu.model.Bill;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object interface for report-related database queries.
 */
public interface ReportDAO {

    // --- Methods for the Admin Dashboard ---
    long getTotalUserCount() throws SQLException;
    long getTotalCustomerCount() throws SQLException;
    long getTotalItemCount() throws SQLException;
    BigDecimal getTodaySalesTotal() throws SQLException;

    // --- Methods for the Full Reports Page ---
    List<Bill> getRecentBills(int limit) throws SQLException;
    Map<String, Integer> getTopSellingItems(int limit) throws SQLException;
}