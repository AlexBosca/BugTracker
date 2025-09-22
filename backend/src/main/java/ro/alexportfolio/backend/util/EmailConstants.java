package ro.alexportfolio.backend.util;

public enum EmailConstants {
    EMAIL_ACCOUNT_CONFIRMATION_SUBJECT("Email confirmation", false),
    EMAIL_ACCOUNT_CONFIRMATION_TITLE("Confirm your email address", false),
    EMAIL_ACCOUNT_CONFIRMATION_CONTENT("Ready to get started? First, verify your email address:", false),
    EMAIL_ACCOUNT_CONFIRMATION_LINK("https://localhost:8081/api/v1/bug-tracker/auth/confirm?token=%s", true),

    EMAIL_ISSUE_ASSIGNATION_SUBJECT("Issue assignation", false),
    EMAIL_ISSUE_ASSIGNATION_TITLE("Issue assignation notification", false),
    EMAIL_ISSUE_ASSIGNATION_CONTENT("You have been assigned on issue", false),

    EMAIL_ISSUE_CLOSING_SUBJECT("Issue closing", false),
    EMAIL_ISSUE_CLOSING_TITLE("Issue closing notification", false),
    EMAIL_ISSUE_CLOSING_CONTENT("You have closed the issue", false),

    EMAIL_ISSUE_UPDATES_SUBJECT("Issue update", false),
    EMAIL_ISSUE_UPDATES_TITLE("Issue update notification", false),
    EMAIL_ISSUE_UPDATES_CONTENT("The issue [%s] %s has been updated.", true),

    EMAIL_UTF8_MESSAGE_ENCODING("utf-8", false),

    EMAIL_ACCOUNT_CONFIRMATION_COMPOSE_AND_SEND("Compose and send the confirmation mail", false),
    EMAIL_NOTIFICATION_COMPOSE_AND_SEND("Compose and send the notification mail", false);

    private final String value;
    private final boolean isFormatted;

    EmailConstants(String value, boolean isFormatted) {
        this.value = value;
        this.isFormatted = isFormatted;
    }

    public String getValue(Object... args) {
        if(isFormatted) {
            return String.format(value, args);
        }

        return value;
    }
}
