package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.pahana.edu.db.DBConnection;
import com.pahana.edu.model.Bill;
import com.pahana.edu.model.BillItem;
import com.pahana.edu.model.Customer;
import com.pahana.edu.model.Item;
import com.pahana.edu.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BillDAOImplTest {

    private BillDAO billDAO = new BillDAOImpl();
    private CustomerDAO customerDAO = new CustomerDAOImpl();
    private ItemDAO itemDAO = new ItemDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    private Customer testCustomer;
    private Item testItem1;
    private Item testItem2;
    private User testUser;

    @BeforeAll
    void setUp() throws SQLException {
        // Create a test user, customer, and items for the bill
        testUser = new User();
        testUser.setUsername("testcashier");
        testUser.setPassword("password123");
        testUser.setFullName("Test Cashier");
        testUser.setRole("CASHIER");
        userDAO.addUser(testUser);
        testUser = userDAO.getUserByUsername("testcashier");

        testCustomer = new Customer();
        testCustomer.setCustomerId(customerDAO.generateNewCustomerId()); // Generate ID
        testCustomer.setFullName("Bill Payer"); // Use FullName
        testCustomer.setEmail("bill.payer@test.com");
        customerDAO.addCustomer(testCustomer);

        testItem1 = new Item();
        testItem1.setItemId(itemDAO.generateNewItemId()); // Generate ID
        testItem1.setItemName("Test Book 1");
        testItem1.setUnitPrice(new BigDecimal("100.00"));
        testItem1.setStockQuantity(10);
        itemDAO.addItem(testItem1);
        
        testItem2 = new Item();
        testItem2.setItemId(itemDAO.generateNewItemId()); // Generate ID
        testItem2.setItemName("Test Pen");
        testItem2.setUnitPrice(new BigDecimal("50.00"));
        testItem2.setStockQuantity(20);
        itemDAO.addItem(testItem2);
    }

    @AfterAll
    void tearDown() throws SQLException {
        // Clean up database in reverse order of creation
        Connection conn = DBConnection.getInstance().getConnection();
        conn.prepareStatement("DELETE FROM bill_items").executeUpdate();
        conn.prepareStatement("DELETE FROM bills").executeUpdate();
        conn.prepareStatement("DELETE FROM customers WHERE email = 'bill.payer@test.com'").executeUpdate();
        conn.prepareStatement("DELETE FROM items WHERE item_name IN ('Test Book 1', 'Test Pen')").executeUpdate();
        conn.prepareStatement("DELETE FROM users WHERE username = 'testcashier'").executeUpdate();
    }

    @Test
    void testSaveBill_Transactional() throws SQLException {
        Bill bill = new Bill();
        // CORRECTED: Generate and set the String bill ID
        String newBillId = billDAO.generateNewBillId();
        bill.setBillId(newBillId);
        bill.setCustomerId(testCustomer.getCustomerId());
        bill.setBilledByUserId(testUser.getUserId());
        bill.setBillDate(new Date(System.currentTimeMillis()));
        
        List<BillItem> items = new ArrayList<>();
        BillItem bItem1 = new BillItem();
        bItem1.setItemId(testItem1.getItemId());
        bItem1.setQuantity(2);
        bItem1.setUnitPrice(testItem1.getUnitPrice());
        items.add(bItem1);
        
        BillItem bItem2 = new BillItem();
        bItem2.setItemId(testItem2.getItemId());
        bItem2.setQuantity(3);
        bItem2.setUnitPrice(testItem2.getUnitPrice());
        items.add(bItem2);
        
        bill.setItems(items);
        bill.setTotalAmount(new BigDecimal("350.00")); // (2 * 100) + (3 * 50)

        assertDoesNotThrow(() -> billDAO.saveBill(bill), "Saving the bill should not throw an exception.");

        // Verify the data was saved correctly
        Connection conn = DBConnection.getInstance().getConnection();
        
        PreparedStatement psBill = conn.prepareStatement("SELECT * FROM bills WHERE bill_id = ?");
        psBill.setString(1, newBillId); // CORRECTED: Use setString
        ResultSet rsBill = psBill.executeQuery();
        assertTrue(rsBill.next(), "An entry should exist in the bills table.");
        assertEquals(0, new BigDecimal("350.00").compareTo(rsBill.getBigDecimal("total_amount")), "Total amount in DB should match.");

        PreparedStatement psItems = conn.prepareStatement("SELECT * FROM bill_items WHERE bill_id = ? ORDER BY item_id");
        psItems.setString(1, newBillId); // CORRECTED: Use setString
        ResultSet rsItems = psItems.executeQuery();
        
        assertTrue(rsItems.next(), "First bill item should exist.");
        assertEquals(testItem1.getItemId(), rsItems.getString("item_id"));
        assertEquals(2, rsItems.getInt("quantity"));
        
        assertTrue(rsItems.next(), "Second bill item should exist.");
        assertEquals(testItem2.getItemId(), rsItems.getString("item_id"));
        assertEquals(3, rsItems.getInt("quantity"));
        
        assertFalse(rsItems.next(), "There should only be two items for this bill.");
        
        System.out.println("--- BillDAO Test PASSED ---");
    }
}