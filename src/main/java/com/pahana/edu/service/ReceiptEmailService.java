package com.pahana.edu.service;

import com.pahana.edu.model.Bill;
import com.pahana.edu.model.Customer;

public interface ReceiptEmailService {
    void sendBillReceipt(Bill bill, Customer customer);
}