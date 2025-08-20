package com.pahana.edu.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        request.getRequestDispatcher("manageItems.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "add"; // Default action
        }

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
                response.sendRedirect("manageItems");
                break;
        }
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String itemName = request.getParameter("itemName");
        BigDecimal unitPrice = new BigDecimal(request.getParameter("unitPrice"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));

        Item newItem = new Item();
        newItem.setItemName(itemName);
        newItem.setUnitPrice(unitPrice);
        newItem.setStockQuantity(stockQuantity);

        itemDAO.addItem(newItem);
        response.sendRedirect("manageItems");
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        String itemName = request.getParameter("itemName");
        BigDecimal unitPrice = new BigDecimal(request.getParameter("unitPrice"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));

        Item item = new Item();
        item.setItemId(itemId);
        item.setItemName(itemName);
        item.setUnitPrice(unitPrice);
        item.setStockQuantity(stockQuantity);

        itemDAO.updateItem(item);
        response.sendRedirect("manageItems");
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        itemDAO.deleteItem(itemId);
        response.sendRedirect("manageItems");
    }
}
