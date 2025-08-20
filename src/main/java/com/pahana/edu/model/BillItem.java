package com.pahana.edu.model;

import java.math.BigDecimal;

public class BillItem {
    private int billItemId;
    private int billId;
    private int itemId;
    private int quantity;
    private BigDecimal unitPrice;

    // --- NEW FIELD ---
    private String itemName; // Used for displaying the item's name on the bill view

    // --- Getters and Setters ---
    public int getBillItemId() { return billItemId; }
    public void setBillItemId(int billItemId) { this.billItemId = billItemId; }
    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    // --- NEW GETTER AND SETTER ---
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
}
