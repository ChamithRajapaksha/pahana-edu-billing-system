package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pahana.edu.model.Item;

class ItemDAOImplTest {

    private ItemDAO itemDAO;

    @BeforeEach
    void setUp() {
        itemDAO = new ItemDAOImpl();
    }

    @Test
    void testFullCrudCycleForItem() {
        System.out.println("Running test: testFullCrudCycleForItem");
        
        // 1. ADD: Create and add a new test item
        Item newItem = new Item();
        // CORRECTED: Generate and set the ID before saving
        String itemId = itemDAO.generateNewItemId();
        newItem.setItemId(itemId);
        newItem.setItemName("Test Book - JUnit");
        newItem.setUnitPrice(new BigDecimal("999.99"));
        newItem.setStockQuantity(10);
        
        itemDAO.addItem(newItem);
        
        // 2. GET: Retrieve the item to verify it was added
        Item retrievedItem = itemDAO.getItemById(itemId);
        
        assertNotNull(retrievedItem, "Item should be found in the database after adding.");
        assertEquals(10, retrievedItem.getStockQuantity(), "Stock quantity should match.");

        // 3. UPDATE: Modify the item's details
        retrievedItem.setItemName("Updated Test Book");
        retrievedItem.setStockQuantity(20);
        itemDAO.updateItem(retrievedItem);
        
        // 4. VERIFY UPDATE: Retrieve the item again and check its new values
        Item updatedItem = itemDAO.getItemById(retrievedItem.getItemId());
        assertNotNull(updatedItem, "Updated item should not be null.");
        assertEquals("Updated Test Book", updatedItem.getItemName(), "Item name should be updated.");
        assertEquals(20, updatedItem.getStockQuantity(), "Stock quantity should be updated.");
        
        // 5. DELETE: Clean up by deleting the test item
        itemDAO.deleteItem(updatedItem.getItemId());
        
        // 6. VERIFY DELETE: Ensure the item is no longer in the database
        Item deletedItem = itemDAO.getItemById(updatedItem.getItemId());
        assertNull(deletedItem, "Item should be null after being deleted.");
    }
}