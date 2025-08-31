package com.pahana.edu.model;

public class Customer {
    // DB Column Constants for safety
    public static final String COL_ID = "customer_id";
    public static final String COL_FULL_NAME = "full_name";
    public static final String COL_NIC = "nic_number";
    public static final String COL_EMAIL = "email";
    public static final String COL_ADDRESS = "address";
    public static final String COL_TELEPHONE = "telephone";

    private String customerId;
    private String fullName;
    private String nicNumber;
    private String email;
    private String address;
    private String telephone;

    // Getters and Setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getNicNumber() { return nicNumber; }
    public void setNicNumber(String nicNumber) { this.nicNumber = nicNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}