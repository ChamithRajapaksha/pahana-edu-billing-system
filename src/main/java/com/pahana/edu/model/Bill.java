package com.pahana.edu.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Bill {
    private int billId;
    private int customerId;
    private int billedByUserId;
    private BigDecimal totalAmount;
    private Date billDate;
    private List<BillItem> items;

    // --- NEW FIELD ---
    private String customerName; // Used for displaying the customer's name on the bill view

    // --- Getters and Setters ---
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getBilledByUserId() { return billedByUserId; }
    public void setBilledByUserId(int billedByUserId) { this.billedByUserId = billedByUserId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }

    // --- NEW GETTER AND SETTER ---
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
