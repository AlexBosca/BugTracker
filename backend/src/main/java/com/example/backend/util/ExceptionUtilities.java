package com.example.backend.util;

public final class ExceptionUtilities {

    public final static String EMAIL_TAKEN = "Email address was already taken.";
    public final static String EMAIL_CONFIRMED = "Email address already confirmed.";
    public final static String EMAIL_NOT_VALID = "Email address not valid.";
    public final static String EMAIL_SEND_FAIL = "Failed to send the email";
    public final static String PASSWORD_NOT_VALID = "Password not valid";
    public final static String USER_WITH_EMAIL_NOT_FOUND = "User with email: %s not found.";
    public final static String USER_WITH_ID_NOT_FOUND = "User with id: %s not found.";
    public final static String TOKEN_NOT_FOUND = "Token not found.";
    public final static String TOKEN_EXPIRED = "Token expired";
    public final static String ISSUE_WITH_ID_NOT_FOUND = "Issue with id: %s not found";
    public final static String ISSUE_STATUS_INVALID_TRANSITION = "Issues with status: %s cannot have the next state: %s";

    public final static String PROJECT_WITH_ID_NOT_FOUND = "Project with id: %s not found";
    public final static String TEAM_WITH_ID_NOT_FOUND_MESSAGE = "Team with id: %s not found";


    private ExceptionUtilities() {  }


}
