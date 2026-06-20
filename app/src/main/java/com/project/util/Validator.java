package com.project.util;

import android.text.TextUtils;
import android.util.Patterns;

public class Validator {

    /**
     * Checks if a phone number is valid.
     * Must be exactly 10 digits and match Android's Patterns.PHONE.
     */
    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        // Assuming standard 10-digit phone number requirement as per previous logic
        if (phone.length() != 10) {
            return false;
        }
        return Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * Checks if a password is valid.
     * Must be at least 6 characters long.
     */
    public static boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return password.length() >= 6;
    }

    /**
     * Checks if an email is valid.
     */
    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
