package com.pahana.edu.service;

import com.pahana.edu.model.Bill;
import com.pahana.edu.model.BillItem;
import com.pahana.edu.model.Customer;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class ReceiptEmailServiceImpl implements ReceiptEmailService {

    private final Properties mailProps = new Properties();
    private boolean isConfigLoaded = false;

    public ReceiptEmailServiceImpl() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("ERROR: Unable to find config.properties in classpath.");
                return;
            }
            mailProps.load(input);
            isConfigLoaded = true;
            System.out.println("Mail configuration loaded successfully.");
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load mail configuration.");
            e.printStackTrace();
        }
    }

    @Override
    public void sendBillReceipt(Bill bill, Customer customer) {
        if (!isConfigLoaded) {
            System.err.println("Email not sent: Mail configuration is missing or failed to load.");
            return;
        }

        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            System.out.println("Email not sent: Customer does not have an email address on file.");
            return;
        }

        final String username = mailProps.getProperty("mail.user");
        final String password = mailProps.getProperty("mail.password");

        Session session = Session.getInstance(mailProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Pahana Edu Bookstore"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
            message.setSubject("Your Receipt from Pahana Edu Bookstore | Bill #" + bill.getBillId());

            String htmlContent = generateHtmlReceipt(bill, customer);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            new Thread(() -> {
                try {
                    Transport.send(message);
                    System.out.println("Receipt email sent successfully to " + customer.getEmail() + " for Bill ID: " + bill.getBillId());
                } catch (MessagingException e) {
                    System.err.println("ERROR: Failed to send email for Bill ID: " + bill.getBillId());
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            System.err.println("ERROR: Could not create email message for Bill ID: " + bill.getBillId());
            e.printStackTrace();
        }
    }

    private String generateHtmlReceipt(Bill bill, Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: sans-serif;'>");
        // CORRECTED: Uses the single getFullName() method
        sb.append("<h2>Thank you for your purchase, ").append(customer.getFullName()).append("!</h2>");
        sb.append("<p>Here is a summary of your bill (ID: ").append(bill.getBillId()).append(").</p>");
        sb.append("<table border='1' cellpadding='10' cellspacing='0' style='border-collapse: collapse;'>");
        sb.append("<tr style='background-color: #f2f2f2;'><th>Item Name</th><th>Quantity</th><th>Unit Price</th></tr>");

        for (BillItem item : bill.getItems()) {
            sb.append("<tr>");
            sb.append("<td>").append(item.getItemName()).append("</td>");
            sb.append("<td>").append(item.getQuantity()).append("</td>");
            sb.append("<td>Rs. ").append(item.getUnitPrice()).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        sb.append("<h3>Total Amount: Rs. ").append(bill.getTotalAmount()).append("</h3>");
        sb.append("<p>We look forward to seeing you again at Pahana Edu Bookstore!</p>");
        sb.append("</body></html>");
        return sb.toString();
    }
}