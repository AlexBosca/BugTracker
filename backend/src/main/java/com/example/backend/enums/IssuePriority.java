package com.example.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IssuePriority {

    LOWEST("LS"), LOW("L"), MEDIUM("M"), HIGH("H"), HIGHEST("HS");

    private final String code;
}
