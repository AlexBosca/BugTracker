package ro.alexportfolio.backend.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.util.EmailSenderUtil;
import ro.alexportfolio.backend.util.EmailTemplateConstans;

@Service
@Qualifier("registration")
public class RegistrationEmailSenderService implements EmailSenderService {
    private final EmailSenderUtil emailSender;

    public RegistrationEmailSenderService(EmailSenderUtil emailSenderParam) {
        this.emailSender = emailSenderParam;
    }

    @Override
    @Transactional
    @Async
    public void sendEmail(final EmailData emailData) {
        emailSender.sendEmail(
            emailData, 
            EmailTemplateConstans.REGISTRATION_EMAIL_TEMPLATE_PATH.getValue()
        );
    }
}
