package com.example.backend.model;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class EmailData {
    private final String recipientName;
    private final String recipientEmail;
    private final String subject;
    private final String title;
    private final String applicationName;
    private final Optional<String> confirmationLink;
    private final Optional<String> notificationContent;
}
