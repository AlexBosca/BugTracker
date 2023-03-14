package com.example.backend.service;

public interface EmailSenderService {
    void send(String recipientName, String recipientEmail, String confirmationLink);
}
