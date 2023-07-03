package com.example.backend.util.issue;

public final class IssueUtilities {

    // Controller related
    public static final String ISSUE_GET_ALL = "Get all issues";
    public static final String ISSUE_GET_BY_ID = "Get issue with id: {}";
    public static final String ISSUE_CREATE = "Create new issue";
    public static final String ISSUE_ASSIGN_TO_DEV = "Assign issue to developer";
    public static final String ISSUE_CHANGE_STATE_MSG = "Move issue with id: {} into state: {}";

    // Service related
    public static final String ISSUE_REQUEST_ALL = "Request all issues";
    public static final String ISSUE_RETURN_ALL = "Return all issues";
    public static final String ISSUE_REQUEST_BY_ID = "Request issue with id: {}";
    public static final String ISSUE_RETURN = "Return issue";
    public static final String ISSUE_CREATE_ON_PROJECT = "Create issue on project with id: {}";
    public static final String ISSUE_CREATED = "Issue created";
    public static final String ISSUE_ASSIGN_BY_ID_TO_DEV = "Assign issue with id: {} to developer with id: {}";
    public static final String ISSUE_ASSIGNED_TO_DEV = "Issue assigned to developer";
    public static final String ISSUE_CLOSE_BY_ID_BY_DEV = "Close issue with id: {} by developer with id: {}";
    public static final String ISSUE_CLOSE_BY_EMAIL_BY_DEV = "Close issue with id: {} by developer with email: {}";
    public static final String ISSUE_CLOSE_BY_DEV = "Issue closed by developer";
    public static final String ISSUE_CHANGE_STATUS_BY_ID = "Change status of issue with id: {} to {}";
    public static final String ISSUE_CHANGED_STATUS = "Issue status changed";
    public static final String ISSUE_USER_ADD_COMMENT_BY_ID = "User: {} add new comment to issue with id: {}";
    public static final String ISSUE_COMMENT_ADDED = "Comment added to issue";

    private IssueUtilities() { }
}
