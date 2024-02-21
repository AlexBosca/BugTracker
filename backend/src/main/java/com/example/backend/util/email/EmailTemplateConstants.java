package com.example.backend.util.email;

public final class EmailTemplateConstants {
    public static final String EMAIL_HTML_TEMPLATE_ROOT = "html/";

    public static final String REGISTRATION_EMAIL_TEMPLATE_NAME = "registration-email";
    public static final String NOTIFICATION_EMAIL_TEMPLATE_NAME = "notification-email";

    public static final String REGISTRATION_EMAIL_TEMPLATE_PATH = EMAIL_HTML_TEMPLATE_ROOT + REGISTRATION_EMAIL_TEMPLATE_NAME;
    public static final String NOTIFICATION_EMAIL_TEMPLATE_PATH = EMAIL_HTML_TEMPLATE_ROOT + NOTIFICATION_EMAIL_TEMPLATE_NAME;

    public static final String EMAIL_CONTEXT_VARIABLE_NAME = "name";
    public static final String EMAIL_CONTEXT_VARIABLE_LINK = "link";
    public static final String EMAIL_CONTEXT_VARIABLE_CONTENT = "content";
    public static final String EMAIL_CONTEXT_VARIABLE_TITLE = "title";
    public static final String EMAIL_CONTEXT_VARIABLE_APPLICATION_NAME = "applicationName";

    private EmailTemplateConstants() {}
}