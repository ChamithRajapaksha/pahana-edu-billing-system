package com.pahana.edu.dao;

import com.pahana.edu.model.Item;
import java.sql.SQLException; // <-- Make sure this import is present
import java.util.List;

public interface ItemDAO {
    String generateNewItemId();
    void addItem(Item item);
    void updateItem(Item item);
    // Add 'throws SQLException' to match the implementation
    void deleteItem(String itemId) throws SQLException; 
    Item getItemById(String itemId);
    List<Item> getAllItems();
    int sumTotalStock() throws SQLException;
    
}