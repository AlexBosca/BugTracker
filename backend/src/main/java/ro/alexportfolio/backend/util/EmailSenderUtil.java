package ro.alexportfolio.backend.util;

import java.util.Optional;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import ro.alexportfolio.backend.exception.EmailSendFailException;
import ro.alexportfolio.backend.model.EmailData;

@Component
public class EmailSenderUtil {

    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    public EmailSenderUtil(JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    public void sendEmail(EmailData emailData, String emailTemplatePath) {
        Context context = createContext(emailData);
        String emailContent = htmlTemplateEngine.process(emailTemplatePath, context);
        sendMimeMessage(emailData.recipientEmail(), emailData.subject(), emailContent);
    }

    private Context createContext(EmailData emailData) {
        Context context = new Context();
        context.setVariable(EmailTemplateConstans.EMAIL_CONTEXT_VARIABLE_NAME.getValue(), emailData.recipientName());

        Optional<String> confirmationLink = emailData.confirmationLink();
        confirmationLink.ifPresent(link -> context.setVariable(EmailTemplateConstans.EMAIL_CONTEXT_VARIABLE_LINK.getValue(), link));

        Optional<String> notificationContent = emailData.notificationContent();
        notificationContent.ifPresent(content -> context.setVariable(EmailTemplateConstans.EMAIL_CONTEXT_VARIABLE_CONTENT.getValue(), content));

        context.setVariable(EmailTemplateConstans.EMAIL_CONTEXT_VARIABLE_TITLE.getValue(), emailData.title());

        context.setVariable(EmailTemplateConstans.EMAIL_CONTEXT_VARIABLE_APPLICATION_NAME.getValue(), emailData.applicationName());

        return context;
    }

    private void sendMimeMessage(String recipientEmail, String subject, String emailContent) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, EmailConstants.EMAIL_UTF8_MESSAGE_ENCODING.getValue());

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
