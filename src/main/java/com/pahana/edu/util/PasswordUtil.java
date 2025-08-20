package com.pahana.edu.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    // Hashes a password using SHA-256 algorithm with consistent UTF-8 encoding.
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Explicitly use UTF-8 to prevent encoding issues between systems.
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            // This should not happen if Java is installed correctly.
            throw new RuntimeException("Could not hash password", e);
        }
    }
    
    // Verifies a plain-text password against a stored hash.
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}
