package ro.alexportfolio.backend.unittests.service;

import org.mockito.Mock;

import static java.util.Optional.of;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.alexportfolio.backend.model.EmailData;
import ro.alexportfolio.backend.service.NotificationEmailSenderService;
import ro.alexportfolio.backend.util.EmailSenderUtil;

@ExtendWith(MockitoExtension.class)
class NotificationEmailSenderServiceTest {
    
    @Mock
    private EmailSenderUtil emailSenderUtil;

    private NotificationEmailSenderService emailSenderService;

    @BeforeEach
    void setUp() {
        emailSenderService = new NotificationEmailSenderService(emailSenderUtil);
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
                .notificationContent(of("This is a test notification content."))
                .build();

        // When
        emailSenderService.sendEmail(emailData);

        // Then
        verify(emailSenderUtil).sendEmail(emailData, "html/notification-email");
    }
}
