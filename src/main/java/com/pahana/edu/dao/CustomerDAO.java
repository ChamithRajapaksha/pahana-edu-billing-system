package com.pahana.edu.dao;

import java.util.List;
import com.pahana.edu.model.Customer;
import java.sql.SQLException;

public interface CustomerDAO {
    String generateNewCustomerId();
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(String customerId);
    Customer getCustomerById(String customerId);
    List<Customer> getAllCustomers();
    int countAllCustomers() throws SQLException;
}