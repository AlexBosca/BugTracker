package ro.alexportfolio.backend.service;

import ro.alexportfolio.backend.model.EmailData;

public interface EmailSenderService {
    void sendEmail(EmailData emailData);
}
