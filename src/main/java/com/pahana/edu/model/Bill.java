package com.pahana.edu.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Bill {
    private String billId;        // Changed
    private String customerId;    // Changed
    private int billedByUserId;
    private BigDecimal totalAmount;
    private Date billDate;
    private List<BillItem> items;
    private String customerName;

    // --- Getters and Setters ---
    public String getBillId() { return billId; }
    public void setBillId(String billId) { this.billId = billId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public int getBilledByUserId() { return billedByUserId; }
    public void setBilledByUserId(int billedByUserId) { this.billedByUserId = billedByUserId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
