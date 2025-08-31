package com.pahana.edu.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.pahana.edu.dao.ItemDAO;
import com.pahana.edu.dao.ItemDAOImpl;
import com.pahana.edu.model.Item;

@WebServlet("/manageItems")
public class ItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ItemDAO itemDAO;

    public void init() {
        itemDAO = new ItemDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Item> itemList = itemDAO.getAllItems();
        request.setAttribute("itemList", itemList);
        request.getRequestDispatcher("/manageItems.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) { action = "list"; }

        switch (action) {
            case "add":
                addItem(request, response);
                break;
            case "update":
                updateItem(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/manageItems");
                break;
        }
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String itemName = request.getParameter("itemName");
        if (itemName == null || itemName.trim().isEmpty()) {
            request.getSession().setAttribute("flashMessage", "Error: Item Name cannot be empty.");
            request.getSession().setAttribute("flashMessageType", "danger");
            response.sendRedirect(request.getContextPath() + "/manageItems");
            return;
        }

        try {
            Item newItem = new Item();
            
            // === CRITICAL FIX 1: Generate the new String ID before adding ===
            String newId = itemDAO.generateNewItemId();
            newItem.setItemId(newId);
            
            newItem.setItemName(itemName);
            newItem.setUnitPrice(new BigDecimal(request.getParameter("unitPrice")));
            newItem.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));

            itemDAO.addItem(newItem);
            request.getSession().setAttribute("flashMessage", "Success! Item '" + itemName + "' was added with ID " + newId);
            request.getSession().setAttribute("flashMessageType", "success");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("flashMessage", "Error: Please enter valid numbers for price and stock.");
            request.getSession().setAttribute("flashMessageType", "danger");
        }
        response.sendRedirect(request.getContextPath() + "/manageItems");
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Item item = new Item();
            // ID is now correctly handled as a String
            item.setItemId(request.getParameter("itemId")); 
            item.setItemName(request.getParameter("itemName"));
            item.setUnitPrice(new BigDecimal(request.getParameter("unitPrice")));
            item.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));

            itemDAO.updateItem(item);
            request.getSession().setAttribute("flashMessage", "Item details updated successfully.");
            request.getSession().setAttribute("flashMessageType", "success");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("flashMessage", "Error: Invalid number format for price or stock.");
            request.getSession().setAttribute("flashMessageType", "danger");
        }
        response.sendRedirect(request.getContextPath() + "/manageItems");
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String itemId = request.getParameter("itemId");
        
        try {
            // This call can now throw an exception, so it must be in a try block.
            itemDAO.deleteItem(itemId); 
            request.getSession().setAttribute("flashMessage", "Item deleted successfully.");
            request.getSession().setAttribute("flashMessageType", "success");
        } catch (SQLException e) {
            // Catch the exception and set an appropriate error message for the user.
            request.getSession().setAttribute("flashMessage", "Error: Cannot delete item. It is already used in a bill.");
            request.getSession().setAttribute("flashMessageType", "danger");
            e.printStackTrace(); // This logs the detailed error to your server console for debugging.
        }
        
        response.sendRedirect(request.getContextPath() + "/manageItems");
    }
    
}