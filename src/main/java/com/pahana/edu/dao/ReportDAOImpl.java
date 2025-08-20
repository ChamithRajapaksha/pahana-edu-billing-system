package com.pahana.edu.dao;

import com.pahana.edu.db.DBConnection;
import com.pahana.edu.model.Bill;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public long getTotalUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public long getTotalCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM customers";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public long getTotalItemCount() throws SQLException {
        String sql = "SELECT SUM(stock_quantity) FROM items";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public BigDecimal getTodaySalesTotal() throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM bills WHERE bill_date = CURDATE()";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal(1);
                return (total == null) ? BigDecimal.ZERO : total;
            }
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<Bill> getRecentBills(int limit) throws SQLException {
        List<Bill> recentBills = new ArrayList<>();
        String sql = "SELECT b.*, c.first_name, c.last_name FROM bills b " +
                     "JOIN customers c ON b.customer_id = c.customer_id " +
                     "ORDER BY b.bill_date DESC, b.bill_id DESC LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setBillId(rs.getInt("bill_id"));
                    bill.setBillDate(rs.getDate("bill_date"));
                    bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                    // Assumes you have added a 'customerName' field to your Bill model
                    bill.setCustomerName(rs.getString("first_name") + " " + rs.getString("last_name"));
                    recentBills.add(bill);
                }
            }
        }
        return recentBills;
    }

    @Override
    public Map<String, Integer> getTopSellingItems(int limit) throws SQLException {
        // Using LinkedHashMap to preserve the order of top items
        Map<String, Integer> topItems = new LinkedHashMap<>();
        String sql = "SELECT i.item_name, SUM(bi.quantity) as total_sold FROM bill_items bi " +
                     "JOIN items i ON bi.item_id = i.item_id " +
                     "GROUP BY i.item_name ORDER BY total_sold DESC LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    topItems.put(rs.getString("item_name"), rs.getInt("total_sold"));
                }
            }
        }
        return topItems;
    }
}
