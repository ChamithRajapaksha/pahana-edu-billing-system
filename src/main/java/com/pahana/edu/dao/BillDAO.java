package com.pahana.edu.dao;

import com.pahana.edu.model.Bill;
import java.sql.SQLException;
import java.util.List; // Make sure this import is present

public interface BillDAO {
    String generateNewBillId();
    String saveBill(Bill bill) throws SQLException;
    Bill getBillById(String billId) throws SQLException;
    List<Bill> getAllBills() throws SQLException; // This line was missing
    java.math.BigDecimal getTodaysSales() throws SQLException;
}