package ro.alexportfolio.backend.exception;

public class IssueOrProjectNotFoundException extends RuntimeException {

    public IssueOrProjectNotFoundException() {
        super(ExceptionMessages.ISSUE_OR_PROJECT_NOT_FOUND.getMessage());
    }
    
}
