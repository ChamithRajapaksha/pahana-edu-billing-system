package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.pahana.edu.db.DBConnection;
import com.pahana.edu.model.Bill;
import com.pahana.edu.model.BillItem;
import com.pahana.edu.model.Customer;
import com.pahana.edu.model.Item;
import com.pahana.edu.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class BillDAOImplTest {

    private BillDAO billDAO;
    private CustomerDAO customerDAO;
    private ItemDAO itemDAO;
    private UserDAO userDAO;

    private Customer testCustomer;
    private Item testItem1;
    private Item testItem2;
    private User testUser;

    // This runs before each test to set up the necessary data
    @BeforeEach
    void setUp() throws SQLException {
        // Instantiate all DAOs needed for the test
        billDAO = new BillDAOImpl();
        customerDAO = new CustomerDAOImpl();
        itemDAO = new ItemDAOImpl();
        userDAO = new UserDAOImpl();

        // Create a test user, customer, and items to make the bill with
        // This ensures our test doesn't depend on pre-existing data
        testUser = new User();
        testUser.setUsername("testcashier");
        testUser.setPassword("password123");
        testUser.setFullName("Test Cashier");
        testUser.setRole("CASHIER");
        userDAO.addUser(testUser);
        testUser = userDAO.getUserByUsername("testcashier"); // Get it back to have the ID

        testCustomer = new Customer();
        testCustomer.setFirstName("Bill");
        testCustomer.setLastName("Payer");
        testCustomer.setEmail("bill.payer@test.com");
        customerDAO.addCustomer(testCustomer);
        testCustomer = customerDAO.getAllCustomers().stream().filter(c -> c.getEmail().equals("bill.payer@test.com")).findFirst().get();

        testItem1 = new Item();
        testItem1.setItemName("Test Book 1");
        testItem1.setUnitPrice(new BigDecimal("100.00"));
        testItem1.setStockQuantity(10);
        itemDAO.addItem(testItem1);
        testItem1 = itemDAO.getAllItems().stream().filter(i -> i.getItemName().equals("Test Book 1")).findFirst().get();
        
        testItem2 = new Item();
        testItem2.setItemName("Test Pen");
        testItem2.setUnitPrice(new BigDecimal("50.00"));
        testItem2.setStockQuantity(20);
        itemDAO.addItem(testItem2);
        testItem2 = itemDAO.getAllItems().stream().filter(i -> i.getItemName().equals("Test Pen")).findFirst().get();
    }

    // This runs after each test to clean up the database
    @AfterEach
    void tearDown() throws SQLException {
        // Clean up database in reverse order of creation to avoid foreign key issues
        // This is crucial for making tests independent of each other
        Connection conn = DBConnection.getInstance().getConnection();
        conn.prepareStatement("DELETE FROM bill_items").executeUpdate();
        conn.prepareStatement("DELETE FROM bills").executeUpdate();
        conn.prepareStatement("DELETE FROM customers WHERE email = 'bill.payer@test.com'").executeUpdate();
        conn.prepareStatement("DELETE FROM items WHERE item_name IN ('Test Book 1', 'Test Pen')").executeUpdate();
        conn.prepareStatement("DELETE FROM users WHERE username = 'testcashier'").executeUpdate();
    }

    @Test
    void testSaveBill_Transactional() throws SQLException {
        // 1. Prepare the Bill object
        Bill bill = new Bill();
        bill.setCustomerId(testCustomer.getCustomerId());
        bill.setBilledByUserId(testUser.getUserId());
        bill.setBillDate(new Date(System.currentTimeMillis()));
        
        List<BillItem> items = new ArrayList<>();
        // Add item 1 (2 books)
        BillItem bItem1 = new BillItem();
        bItem1.setItemId(testItem1.getItemId());
        bItem1.setQuantity(2); // 2 * 100.00 = 200.00
        bItem1.setUnitPrice(testItem1.getUnitPrice());
        items.add(bItem1);
        
        // Add item 2 (3 pens)
        BillItem bItem2 = new BillItem();
        bItem2.setItemId(testItem2.getItemId());
        bItem2.setQuantity(3); // 3 * 50.00 = 150.00
        bItem2.setUnitPrice(testItem2.getUnitPrice());
        items.add(bItem2);
        
        bill.setItems(items);
        bill.setTotalAmount(new BigDecimal("350.00")); // 200 + 150

        // 2. Execute the saveBill method
        assertDoesNotThrow(() -> billDAO.saveBill(bill), "Saving the bill should not throw an exception.");

        // 3. Verify the data was saved correctly in BOTH tables
        Connection conn = DBConnection.getInstance().getConnection();
        
        // Verify the 'bills' table entry
        PreparedStatement psBill = conn.prepareStatement("SELECT * FROM bills WHERE customer_id = ?");
        psBill.setInt(1, testCustomer.getCustomerId());
        ResultSet rsBill = psBill.executeQuery();
        assertTrue(rsBill.next(), "An entry should exist in the bills table.");
        assertEquals(0, new BigDecimal("350.00").compareTo(rsBill.getBigDecimal("total_amount")), "Total amount in DB should match.");
        int savedBillId = rsBill.getInt("bill_id");

        // Verify the 'bill_items' table entries
        PreparedStatement psItems = conn.prepareStatement("SELECT * FROM bill_items WHERE bill_id = ?");
        psItems.setInt(1, savedBillId);
        ResultSet rsItems = psItems.executeQuery();
        
        assertTrue(rsItems.next(), "First bill item should exist.");
        assertEquals(2, rsItems.getInt("quantity"));
        
        assertTrue(rsItems.next(), "Second bill item should exist.");
        assertEquals(3, rsItems.getInt("quantity"));
        
        assertFalse(rsItems.next(), "There should only be two items for this bill.");
        
        System.out.println("--- BillDAO Test PASSED ---");
    }
}