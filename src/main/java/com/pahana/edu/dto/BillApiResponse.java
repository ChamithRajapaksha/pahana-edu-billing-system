package com.pahana.edu.dto;

import java.math.BigDecimal;

public class BillApiResponse {
    private String billId; // Changed to String
    private BigDecimal totalAmount;

    public BillApiResponse(String billId, BigDecimal totalAmount) {
        this.billId = billId;
        this.totalAmount = totalAmount;
    }
    
    // Getters & Setters
    public String getBillId() { return billId; }
    public void setBillId(String billId) { this.billId = billId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
