package com.pahana.edu.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customerList = customerDAO.getAllCustomers();
        request.setAttribute("customerList", customerList);
        request.getRequestDispatcher("/manageCustomers.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

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
                response.sendRedirect(request.getContextPath() + "/manageCustomers");
                break;
        }
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fullName = request.getParameter("fullName");

        // Server-side validation
        if (fullName == null || fullName.trim().isEmpty()) {
            request.getSession().setAttribute("flashMessage", "Error: Full Name cannot be empty.");
            request.getSession().setAttribute("flashMessageType", "danger");
            response.sendRedirect(request.getContextPath() + "/manageCustomers");
            return;
        }
        
        Customer customer = new Customer();
        customer.setCustomerId(customerDAO.generateNewCustomerId());
        customer.setFullName(fullName);
        customer.setNicNumber(request.getParameter("nicNumber"));
        customer.setEmail(request.getParameter("email"));
        customer.setAddress(request.getParameter("address"));
        customer.setTelephone(request.getParameter("telephone"));
        
        customerDAO.addCustomer(customer);
        
        request.getSession().setAttribute("flashMessage", "Success! Customer '" + fullName + "' was added.");
        request.getSession().setAttribute("flashMessageType", "success");
        response.sendRedirect(request.getContextPath() + "/manageCustomers");
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Customer customer = new Customer();
        customer.setCustomerId(request.getParameter("customerId"));
        customer.setFullName(request.getParameter("fullName"));
        customer.setNicNumber(request.getParameter("nicNumber"));
        customer.setEmail(request.getParameter("email"));
        customer.setAddress(request.getParameter("address"));
        customer.setTelephone(request.getParameter("telephone"));

        customerDAO.updateCustomer(customer);
        request.getSession().setAttribute("flashMessage", "Customer details updated successfully.");
        request.getSession().setAttribute("flashMessageType", "success");
        response.sendRedirect(request.getContextPath() + "/manageCustomers");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String customerId = request.getParameter("customerId");
        customerDAO.deleteCustomer(customerId);
        request.getSession().setAttribute("flashMessage", "Customer deleted successfully.");
        request.getSession().setAttribute("flashMessageType", "success");
        response.sendRedirect(request.getContextPath() + "/manageCustomers");
    }
}