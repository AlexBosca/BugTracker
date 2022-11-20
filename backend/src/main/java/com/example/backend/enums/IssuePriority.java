package com.example.backend.enums;

public enum IssuePriority {

    LOWEST("LS"), LOW("L"), MEDIUM("M"), HIGH("H"), HIGHEST("HS");

    private final String code;

    IssuePriority(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
