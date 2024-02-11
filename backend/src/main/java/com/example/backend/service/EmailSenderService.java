package com.example.backend.service;

import com.example.backend.model.EmailData;

public interface EmailSenderService {
    void send(EmailData emailData);
}
