package com.example.backend.model;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class EmailData {
    private final String recipientName;
    private final String recipientEmail;
    private final String subject;
    private final Optional<String> confirmationLink;
}
