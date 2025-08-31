package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pahana.edu.model.Customer;

class CustomerDAOImplTest {
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        customerDAO = new CustomerDAOImpl();
    }

    @Test
    void testFullCrudCycleForCustomer() {
        System.out.println("--- Running CustomerDAO Test ---");
        
        // 1. ADD a new test customer
        Customer newCustomer = new Customer();
        // CORRECTED: Generate and set the ID before saving
        String customerId = customerDAO.generateNewCustomerId();
        newCustomer.setCustomerId(customerId);
        // CORRECTED: Use setFullName
        newCustomer.setFullName("JUnit Tester");
        newCustomer.setEmail("junit.test@example.com");
        newCustomer.setNicNumber("999999999V");
        
        customerDAO.addCustomer(newCustomer);
        System.out.println("ADD: Customer 'JUnit Tester' added with ID " + customerId);

        // 2. GET the customer back using the known ID
        Customer retrievedCustomer = customerDAO.getCustomerById(customerId);
        
        assertNotNull(retrievedCustomer, "Customer should be found after adding.");
        assertEquals("JUnit Tester", retrievedCustomer.getFullName());
        System.out.println("GET: Customer successfully retrieved from DB.");

        // 3. UPDATE the customer's details
        retrievedCustomer.setFullName("JUnit Updated");
        customerDAO.updateCustomer(retrievedCustomer);
        System.out.println("UPDATE: Customer name updated.");
        
        // 4. VERIFY the update
        Customer updatedCustomer = customerDAO.getCustomerById(retrievedCustomer.getCustomerId());
        // CORRECTED: Check getFullName
        assertEquals("JUnit Updated", updatedCustomer.getFullName(), "Full name should be updated.");
        System.out.println("VERIFY: Customer name update confirmed.");
        
        // 5. DELETE the customer to clean up
        customerDAO.deleteCustomer(updatedCustomer.getCustomerId());
        System.out.println("DELETE: Customer deleted.");
        
        // 6. VERIFY the deletion
        Customer deletedCustomer = customerDAO.getCustomerById(updatedCustomer.getCustomerId());
        assertNull(deletedCustomer, "Customer should be null after being deleted.");
        System.out.println("VERIFY: Customer deletion confirmed.");
        System.out.println("--- CustomerDAO Test PASSED ---");
    }
}