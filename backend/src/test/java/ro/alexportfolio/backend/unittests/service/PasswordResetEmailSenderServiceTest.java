package ro.alexportfolio.backend.unittests.service;

import static java.util.Optional.of;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.service.PasswordResetEmailSenderService;
import ro.alexportfolio.backend.util.EmailSenderUtil;

@ExtendWith(MockitoExtension.class)
class PasswordResetEmailSenderServiceTest {
    
    @Mock
    private EmailSenderUtil emailSenderUtil;

    private PasswordResetEmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        emailSenderService = new PasswordResetEmailSenderService(emailSenderUtil);
    }

    @Test
    void sendEmail_Success() {
        // Given
        EmailData emailData = new EmailData.Builder()
                .recipientName("John Doe")
                .recipientEmail("john.doe@mail.com")
                .subject("Test Subject")
                .title("Test Title")
                .applicationName("TestApp")
                .notificationContent(of("This is a test password reset content."))
                .build();

        // When
        emailSenderService.sendEmail(emailData);

        // Then
        verify(emailSenderUtil).sendEmail(emailData, "html/password-reset-email");
    }
}
