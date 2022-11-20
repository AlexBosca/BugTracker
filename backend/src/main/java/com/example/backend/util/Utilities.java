package com.example.backend.util;

public final class Utilities {

    public final static String CONFIRMATION_LINK = "http://localhost:8081/api/v1/bug-tracker/registration/confirm?token=%s";

    private Utilities() {   }

    public static String formattedString(String message, String... args) {
        return String.format(message, args);
    }
}
