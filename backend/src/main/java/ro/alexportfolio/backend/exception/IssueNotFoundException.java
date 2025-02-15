package ro.alexportfolio.backend.exception;

public class IssueNotFoundException extends RuntimeException {

    public IssueNotFoundException() {
        super(ExceptionMessages.ISSUE_NOT_FOUND.getMessage());
    }
    
}
