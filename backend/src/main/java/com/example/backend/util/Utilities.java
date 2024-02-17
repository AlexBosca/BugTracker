package com.example.backend.util;

public final class Utilities {

    public static final long CREDENTIALS_VALIDITY_IN_DAYS = 60L;

    public static final String API_PREFIX = "/api/v1/bug-tracker";
    public static final String AUTHENTICATION_REQUEST_PATH = API_PREFIX + "/authentication";

    private Utilities() { }

    public static String formattedString(String message, String... args) {
        return String.format(message, args);
    }
}
