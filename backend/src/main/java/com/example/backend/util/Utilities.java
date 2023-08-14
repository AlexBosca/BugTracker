package com.example.backend.util;

public final class Utilities {

    public static final String CONFIRMATION_LINK = "http://localhost:8081/api/v1/bug-tracker/authentication/confirm?token=%s";
    public static final long CREDENTIALS_VALIDITY_IN_DAYS = 60L;

    private Utilities() { }

    public static String formattedString(String message, String... args) {
        return String.format(message, args);
    }
}
