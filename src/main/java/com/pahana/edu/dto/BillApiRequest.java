package com.pahana.edu.dto;

import java.math.BigDecimal;
import java.util.List;

public class BillApiRequest {
    private String customerId; // Changed to String
    private BigDecimal discountPct;
    private List<BillLineItem> lines;

    public static class BillLineItem {
        private String itemId; // Changed to String
        private int qty;

        // Getters & Setters
        public String getItemId() { return itemId; }
        public void setItemId(String itemId) { this.itemId = itemId; }
        public int getQty() { return qty; }
        public void setQty(int qty) { this.qty = qty; }
    }
    
    // Getters & Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public BigDecimal getDiscountPct() { return discountPct; }
    public void setDiscountPct(BigDecimal discountPct) { this.discountPct = discountPct; }
    public List<BillLineItem> getLines() { return lines; }
    public void setLines(List<BillLineItem> lines) { this.lines = lines; }
}