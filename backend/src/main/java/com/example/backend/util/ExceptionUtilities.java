package com.example.backend.util;

public final class ExceptionUtilities {

    public static final String EMAIL_TAKEN = "Email address was already taken";
    public static final String EMAIL_CONFIRMED = "Email address already confirmed";
    public static final String EMAIL_NOT_CONFIRMED = "Email address not confirmed";
    public static final String EMAIL_SEND_FAIL = "Failed to send the email";
    public static final String USER_WITH_EMAIL_NOT_FOUND = "User with email: %s not found";
    public static final String USER_WITH_ID_NOT_FOUND = "User with id: %s not found";
    public static final String USER_CREDENTIALS_NOT_VALID = "User's credentials aren't valid";
    public static final String USER_PASSWORDS_NOT_MATCHING = "User's passwords aren't matching";
    public static final String USER_CREDENTIALS_EXPIRED = "User's credentials has expired";
    public static final String ROLE_WITH_CODE_NOT_FOUND = "Role with code : %s not found";
    public static final String TOKEN_NOT_FOUND = "Token not found";
    public static final String TOKEN_EXPIRED = "Token expired";
    
    public static final String ISSUE_WITH_ID_NOT_FOUND = "Issue with id: %s not found";
    public static final String ISSUE_STATUS_INVALID_TRANSITION = "Issues with status: %s cannot have the next state: %s";
    public static final String ISSUE_ALREADY_CREATED = "Issue with id: %s already created";

    
    public static final String PROJECT_WITH_ID_NOT_FOUND = "Project with id: %s not found";
    public static final String PROJECT_ALREADY_CREATED = "Project with id: %s already created";
    
    public static final String TEAM_WITH_ID_NOT_FOUND = "Team with id: %s not found";
    public static final String TEAM_ALREADY_CREATED = "Team with id: %s already created";


    private ExceptionUtilities() { }


}
