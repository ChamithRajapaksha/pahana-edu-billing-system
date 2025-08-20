package com.pahana.edu.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pahana.edu.dao.CustomerDAO;
import com.pahana.edu.dao.CustomerDAOImpl;
import com.pahana.edu.model.Customer;

@WebServlet("/manageCustomers")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;

    public void init() {
        customerDAO = new CustomerDAOImpl();
    }

    // Handles GET requests (e.g., clicking the link in the sidebar)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Get all customers from the DAO
        List<Customer> customerList = customerDAO.getAllCustomers();
        
        // 2. Set the list as an attribute for the JSP to access
        request.setAttribute("customerList", customerList);
        
        // 3. Forward the request to the JSP page for display
        request.getRequestDispatcher("manageCustomers.jsp").forward(request, response);
    }

    // Handles POST requests (e.g., submitting the Add, Edit, or Delete forms)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        // Use a switch statement to handle different actions
        switch (action) {
            case "add":
                addCustomer(request, response);
                break;
            case "update":
                updateCustomer(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            default:
                // If action is unknown, just redirect back to the list
                response.sendRedirect("manageCustomers");
                break;
        }
    }

    // --- Private Helper Methods for each action ---

    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Customer customer = new Customer();
        customer.setFirstName(request.getParameter("firstName"));
        customer.setLastName(request.getParameter("lastName"));
        customer.setNicNumber(request.getParameter("nicNumber"));
        customer.setEmail(request.getParameter("email"));
        customer.setAddress(request.getParameter("address"));
        customer.setTelephone(request.getParameter("telephone"));
        
        customerDAO.addCustomer(customer);
        response.sendRedirect("manageCustomers"); // Redirect to refresh the list
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Customer customer = new Customer();
        customer.setCustomerId(Integer.parseInt(request.getParameter("customerId")));
        customer.setFirstName(request.getParameter("firstName"));
        customer.setLastName(request.getParameter("lastName"));
        customer.setNicNumber(request.getParameter("nicNumber"));
        customer.setEmail(request.getParameter("email"));
        customer.setAddress(request.getParameter("address"));
        customer.setTelephone(request.getParameter("telephone"));

        customerDAO.updateCustomer(customer);
        response.sendRedirect("manageCustomers");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        customerDAO.deleteCustomer(customerId);
        response.sendRedirect("manageCustomers");
    }
}