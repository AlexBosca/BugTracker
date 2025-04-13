package ro.alexportfolio.backend.exception;

public class EmailSendFailException extends RuntimeException {
    public EmailSendFailException() {
        super(ExceptionMessages.EMAIL_SEND_FAIL.getMessage());
    }
    
}
