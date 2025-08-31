package com.pahana.edu.dao;

import com.pahana.edu.db.DBConnection;
import com.pahana.edu.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public String generateNewItemId() {
        String newId = "ITID01";
        String sql = "SELECT item_id FROM items ORDER BY item_id DESC LIMIT 1";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String lastId = rs.getString("item_id");
                int numPart = Integer.parseInt(lastId.substring(4));
                numPart++;
                newId = String.format("ITID%02d", numPart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    @Override
    public void addItem(Item item) {
        String sql = "INSERT INTO items (item_id, item_name, unit_price, stock_quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getItemName());
            ps.setBigDecimal(3, item.getUnitPrice());
            ps.setInt(4, item.getStockQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItem(Item item) {
        String sql = "UPDATE items SET item_name = ?, unit_price = ?, stock_quantity = ? WHERE item_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemName());
            ps.setBigDecimal(2, item.getUnitPrice());
            ps.setInt(3, item.getStockQuantity());
            ps.setString(4, item.getItemId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteItem(String itemId) throws SQLException {
        String sql = "UPDATE items SET status = 'INACTIVE' WHERE item_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Instead of just printing, re-throw the exception
            // so the calling layer (servlet) can handle it.
            throw e;
        }
    }
    
    // Other methods (getItemById, getAllItems, extractItemFromResultSet) remain the same as the previous response...
    @Override
    public Item getItemById(String itemId) {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractItemFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE status = 'ACTIVE' ORDER BY item_id";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(extractItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private Item extractItemFromResultSet(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemId(rs.getString("item_id"));
        item.setItemName(rs.getString("item_name"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setStockQuantity(rs.getInt("stock_quantity"));
        return item;
    }
    @Override
    public int sumTotalStock() throws SQLException {
        String sql = "SELECT SUM(stock_quantity) FROM items WHERE status = 'ACTIVE'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
