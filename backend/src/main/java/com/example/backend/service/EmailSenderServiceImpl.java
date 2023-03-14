package com.example.backend.service;

import com.example.backend.exception.registration.EmailSendFailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@AllArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final TemplateEngine htmlTemplateEngine;

    @Override
    @Async
    public void send(String recipientName, String recipientEmail, String confirmationLink) {
        log.info("Compose and send the confirmation mail");

        Context context = new Context();
        context.setVariable("name", recipientName);
        context.setVariable("link", confirmationLink);
        String emailContent = htmlTemplateEngine.process("html/registration-email", context);

        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(emailContent, true);
            helper.setTo(recipientEmail);
            helper.setSubject("Confirm your email");
            helper.setFrom("no-reply@bugtracker.com");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new EmailSendFailException();
        }
    }
}
