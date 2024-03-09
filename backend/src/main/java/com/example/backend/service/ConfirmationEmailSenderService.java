package com.example.backend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.example.backend.util.email.EmailTemplateConstants.REGISTRATION_EMAIL_TEMPLATE_PATH;
import static com.example.backend.util.email.EmailConstants.EMAIL_ACCOUNT_CONFIRMATION_COMPOSE_AND_SEND;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.EmailData;

@Slf4j
@Service
@Qualifier("confirmation")
@AllArgsConstructor
public class ConfirmationEmailSenderService implements EmailSenderService {
    private final EmailSenderUtil emailSender;

    @Override
    @Transactional
    @Async
    public void send(EmailData emailData) {
        log.info(EMAIL_ACCOUNT_CONFIRMATION_COMPOSE_AND_SEND);

        emailSender.sendEmail(emailData, REGISTRATION_EMAIL_TEMPLATE_PATH);
    }
}
