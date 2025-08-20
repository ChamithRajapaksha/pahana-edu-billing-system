package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

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
        String testItemName = "Test Book - JUnit";
        Item newItem = new Item();
        newItem.setItemName(testItemName);
        newItem.setUnitPrice(new BigDecimal("999.99"));
        newItem.setStockQuantity(10);
        
        itemDAO.addItem(newItem);
        
        // 2. GET: Retrieve the item to verify it was added
        // We need to find the ID first, as it's auto-incremented
        List<Item> allItems = itemDAO.getAllItems();
        Item retrievedItem = allItems.stream()
                                     .filter(item -> testItemName.equals(item.getItemName()))
                                     .findFirst()
                                     .orElse(null);
        
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
