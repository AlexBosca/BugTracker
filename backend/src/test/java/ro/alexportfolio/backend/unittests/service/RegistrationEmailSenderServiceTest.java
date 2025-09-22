package ro.alexportfolio.backend.unittests.service;

import static java.util.Optional.of;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.service.RegistrationEmailSenderService;
import ro.alexportfolio.backend.util.EmailSenderUtil;

@ExtendWith(MockitoExtension.class)
class RegistrationEmailSenderServiceTest {
    
    @Mock
    private EmailSenderUtil emailSenderUtil;

    private RegistrationEmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        emailSenderService = new RegistrationEmailSenderService(emailSenderUtil);
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
                .notificationContent(of("This is a test registration content."))
                .build();

        // When
        emailSenderService.sendEmail(emailData);

        // Then
        verify(emailSenderUtil).sendEmail(emailData, "html/registration-email");
    }
}
