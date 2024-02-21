package com.example.backend.service;

import static com.example.backend.util.email.EmailConstants.EMAIL_UTF8_MESSAGE_ENCODING;
import static com.example.backend.util.email.EmailTemplateConstants.EMAIL_CONTEXT_VARIABLE_APPLICATION_NAME;
import static com.example.backend.util.email.EmailTemplateConstants.EMAIL_CONTEXT_VARIABLE_CONTENT;
import static com.example.backend.util.email.EmailTemplateConstants.EMAIL_CONTEXT_VARIABLE_LINK;
import static com.example.backend.util.email.EmailTemplateConstants.EMAIL_CONTEXT_VARIABLE_NAME;
import static com.example.backend.util.email.EmailTemplateConstants.EMAIL_CONTEXT_VARIABLE_TITLE;

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

    public void sendEmail(EmailData emailData, String emailTemplatePath) {
        Context context = createContext(emailData);
        String emailContent = htmlTemplateEngine.process(emailTemplatePath, context);
        sendMimeMessage(emailData.getRecipientEmail(), emailData.getSubject(), emailContent);
    }

    private Context createContext(EmailData emailData) {
        Context context = new Context();
        context.setVariable(EMAIL_CONTEXT_VARIABLE_NAME, emailData.getRecipientName());

        Optional<String> confirmationLink = emailData.getConfirmationLink();
        confirmationLink.ifPresent(link -> context.setVariable(EMAIL_CONTEXT_VARIABLE_LINK, link));

        Optional<String> notificationContent = emailData.getNotificationContent();
        notificationContent.ifPresent(content -> context.setVariable(EMAIL_CONTEXT_VARIABLE_CONTENT, content));

        context.setVariable(EMAIL_CONTEXT_VARIABLE_TITLE, emailData.getTitle());

        context.setVariable(EMAIL_CONTEXT_VARIABLE_APPLICATION_NAME, emailData.getApplicationName());

        return context;
    }

    private void sendMimeMessage(String recipientEmail, String subject, String emailContent) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, EMAIL_UTF8_MESSAGE_ENCODING);

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
