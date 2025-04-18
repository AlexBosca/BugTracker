package ro.alexportfolio.backend.exception;

public enum ExceptionMessages {
    PROJECT_NOT_FOUND("Project not found"),
    ISSUE_NOT_FOUND("Issue not found"),
    ISSUE_OR_PROJECT_NOT_FOUND("Issue or project not found"),
    EMAIL_SEND_FAIL("Failed to send the email");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
