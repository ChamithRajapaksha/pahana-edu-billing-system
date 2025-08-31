
package com.pahana.edu.dao;

import com.pahana.edu.db.DBConnection;
import com.pahana.edu.model.Bill;
import com.pahana.edu.model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillDAOImpl implements BillDAO {

	@Override
	public String generateNewBillId() {
	    String seed = "BIID01";
	    String sql = "SELECT bill_id FROM bills ORDER BY bill_id DESC LIMIT 1";
	    try (Connection conn = DBConnection.getInstance().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        if (rs.next()) {
	            String last = rs.getString(1);
	            if (last != null && !last.trim().isEmpty()) {
	                Pattern p = Pattern.compile("(\\d+)$");
	                Matcher m = p.matcher(last.trim());
	                if (m.find()) {
	                    int next = Integer.parseInt(m.group(1)) + 1;
	                    String prefix = last.substring(0, m.start(1));
	                    if (prefix.isEmpty()) {
	                        prefix = "BIID";
	                    }
	                    int width = m.group(1).length();
	                    if (width < 2) {
	                        width = 2;
	                    }
	                    return String.format("%s%0" + width + "d", prefix, next);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return seed;
	}

    @Override
    public String saveBill(Bill bill) throws SQLException {
        if (bill == null) throw new IllegalArgumentException("bill cannot be null");

        if (bill.getBillId() == null || bill.getBillId().trim().isEmpty()) {
            bill.setBillId(generateNewBillId());
        }
        String billId = bill.getBillId();

        String sqlBill = "INSERT INTO bills (bill_id, customer_id, billed_by_user_id, total_amount, bill_date) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        boolean oldAutoCommit = true;
        try {
            conn = DBConnection.getInstance().getConnection();
            oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlBill)) {
                ps.setString(1, billId);
                ps.setString(2, bill.getCustomerId());
                ps.setInt(3, bill.getBilledByUserId());
                ps.setBigDecimal(4, bill.getTotalAmount());
                ps.setDate(5, bill.getBillDate());
                ps.executeUpdate();
            }

            if (bill.getItems() != null) {
                try (PreparedStatement ps = conn.prepareStatement(sqlItem)) {
                    for (BillItem bi : bill.getItems()) {
                        ps.setString(1, billId);
                        ps.setString(2, bi.getItemId());
                        ps.setInt(3, bi.getQuantity());
                        ps.setBigDecimal(4, bi.getUnitPrice());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.commit();
            return billId;
        } catch (SQLException ex) {
            if (conn != null) conn.rollback();
            throw ex;
        } finally {
            if (conn != null) conn.setAutoCommit(oldAutoCommit);
        }
    }

    @Override
    public Bill getBillById(String billId) throws SQLException {
        // FIX 1: The SQL query now also selects the customer's ID.
        String headSql =
            "SELECT b.bill_id, b.bill_date, b.total_amount, b.customer_id, c.full_name AS customer_name " +
            "FROM bills b LEFT JOIN customers c ON b.customer_id = c.customer_id " +
            "WHERE b.bill_id = ?";

        String itemsSql =
            "SELECT bi.bill_item_id, bi.bill_id, bi.item_id, bi.quantity, bi.unit_price, i.item_name " +
            "FROM bill_items bi LEFT JOIN items i ON bi.item_id = i.item_id " +
            "WHERE bi.bill_id = ?";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            Bill bill = null;
            try (PreparedStatement ps = conn.prepareStatement(headSql)) {
                ps.setString(1, billId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        bill = new Bill();
                        bill.setBillId(rs.getString("bill_id"));
                        bill.setBillDate(rs.getDate("bill_date"));
                        bill.setTotalAmount(rs.getBigDecimal("total_amount"));
                        bill.setCustomerName(rs.getString("customer_name"));
                        // FIX 2: The customer ID is now set on the Bill object.
                        bill.setCustomerId(rs.getString("customer_id"));
                    } else {
                        return null; // No bill found with that ID
                    }
                }
            }

            // This part for fetching items was already correct.
            List<BillItem> items = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(itemsSql)) {
                ps.setString(1, billId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        BillItem it = new BillItem();
                        it.setBillItemId(rs.getInt("bill_item_id"));
                        it.setBillId(rs.getString("bill_id"));
                        it.setItemId(rs.getString("item_id"));
                        it.setItemName(rs.getString("item_name"));
                        it.setQuantity(rs.getInt("quantity"));
                        it.setUnitPrice(rs.getBigDecimal("unit_price"));
                        items.add(it);
                    }
                }
            }
            bill.setItems(items);
            return bill;
        }
    }

    @Override
    public List<Bill> getAllBills() throws SQLException {
        // NOTE: Also updated this method to include customer_id for consistency.
        String sql =
            "SELECT b.bill_id, b.bill_date, b.total_amount, b.customer_id, c.full_name AS customer_name " +
            "FROM bills b LEFT JOIN customers c ON b.customer_id = c.customer_id " +
            "ORDER BY b.bill_date DESC, b.bill_id DESC";

        List<Bill> out = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bill b = new Bill();
                b.setBillId(rs.getString("bill_id"));
                b.setBillDate(rs.getDate("bill_date"));
                b.setTotalAmount(rs.getBigDecimal("total_amount"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setCustomerId(rs.getString("customer_id"));
                out.add(b);
            }
        }
        return out;
    }
    
    @Override
    public java.math.BigDecimal getTodaysSales() throws SQLException {
        String sql = "SELECT SUM(total_amount) FROM bills WHERE DATE(bill_date) = CURDATE()";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                java.math.BigDecimal total = rs.getBigDecimal(1);
                return (total == null) ? java.math.BigDecimal.ZERO : total;
            }
        }
        return java.math.BigDecimal.ZERO;
    }
}