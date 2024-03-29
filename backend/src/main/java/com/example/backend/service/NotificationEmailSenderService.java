package com.example.backend.service;

import static com.example.backend.util.email.EmailTemplateConstants.NOTIFICATION_EMAIL_TEMPLATE_PATH;
import static com.example.backend.util.email.EmailConstants.EMAIL_NOTIFICATION_COMPOSE_AND_SEND;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.EmailData;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Qualifier("notification")
@AllArgsConstructor
public class NotificationEmailSenderService implements EmailSenderService {
    private final EmailSenderUtil emailSender;

    @Override
    @Transactional
    @Async
    public void send(EmailData emailData) {
        log.info(EMAIL_NOTIFICATION_COMPOSE_AND_SEND);

        emailSender.sendEmail(emailData, NOTIFICATION_EMAIL_TEMPLATE_PATH);
    }
}
