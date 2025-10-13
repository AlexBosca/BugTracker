package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.util.EmailSenderUtil;
import ro.alexportfolio.backend.util.EmailTemplateConstans;

@Service
@Qualifier("passwordReset")
public class PasswordResetEmailSenderService implements EmailSenderService {
    private final EmailSenderUtil emailSender;

    public PasswordResetEmailSenderService(final EmailSenderUtil emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    @Transactional
    @Async
    public void sendEmail(EmailData emailData) {
        emailSender.sendEmail(emailData, EmailTemplateConstans.PASSWORD_RESET_EMAIL_TEMPLATE_PATH.getValue());
    }
}
