package com.example.backend.service;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.backend.exception.registration.EmailSendFailException;
import com.example.backend.model.EmailData;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailSenderUtil {
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    public void sendEmail(EmailData emailData, String emailTemplate) {
        Context context = createContext(emailData.getRecipientName(), emailData.getConfirmationLink());
        String emailContent = htmlTemplateEngine.process("html/" + emailTemplate, context);
        sendMimeMessage(emailData.getRecipientEmail(), emailData.getSubject(), emailContent);
    }

    private Context createContext(String recipientEmail, Optional<String> confirmationLink) {
        Context context = new Context();
        context.setVariable("name", recipientEmail);

        confirmationLink.ifPresent(link -> context.setVariable("link", link));

        return context;
    }

    private void sendMimeMessage(String recipientEmail, String subject, String emailContent) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(emailContent, true);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setFrom("no-reply@bugtracker.com");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new EmailSendFailException();
        }
    }
}
