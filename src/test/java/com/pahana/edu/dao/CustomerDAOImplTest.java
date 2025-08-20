package com.pahana.edu.dao;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pahana.edu.model.Customer;

import java.util.List;

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
        newCustomer.setFirstName("JUnit");
        newCustomer.setLastName("Tester");
        newCustomer.setEmail("junit.test@example.com");
        newCustomer.setNicNumber("999999999V");
        customerDAO.addCustomer(newCustomer);
        System.out.println("ADD: Customer 'JUnit Tester' added.");

        // 2. GET the customer back to verify it was added
        List<Customer> allCustomers = customerDAO.getAllCustomers();
        Customer retrievedCustomer = allCustomers.stream()
                                             .filter(c -> "junit.test@example.com".equals(c.getEmail()))
                                             .findFirst()
                                             .orElse(null);
        
        assertNotNull(retrievedCustomer, "Customer should be found after adding.");
        System.out.println("GET: Customer successfully retrieved from DB.");

        // 3. UPDATE the customer's details
        retrievedCustomer.setFirstName("JUnit Updated");
        customerDAO.updateCustomer(retrievedCustomer);
        System.out.println("UPDATE: Customer name updated.");
        
        // 4. VERIFY the update
        Customer updatedCustomer = customerDAO.getCustomerById(retrievedCustomer.getCustomerId());
        assertEquals("JUnit Updated", updatedCustomer.getFirstName(), "First name should be updated.");
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
