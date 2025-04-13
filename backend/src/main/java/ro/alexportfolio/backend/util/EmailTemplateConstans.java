package ro.alexportfolio.backend.util;

public enum EmailTemplateConstans {
    EMAIL_HTML_TEMPLATE_ROOT("html/"),

    REGISTRATION_EMAIL_TEMPLATE_NAME("registration-email"),
    NOTIFICATION_EMAIL_TEMPLATE_NAME("notification-email"),

    REGISTRATION_EMAIL_TEMPLATE_PATH( EMAIL_HTML_TEMPLATE_ROOT.getValue() + REGISTRATION_EMAIL_TEMPLATE_NAME.getValue()),
    NOTIFICATION_EMAIL_TEMPLATE_PATH( EMAIL_HTML_TEMPLATE_ROOT.getValue() + NOTIFICATION_EMAIL_TEMPLATE_NAME.getValue()),

    EMAIL_CONTEXT_VARIABLE_NAME("name"),
    EMAIL_CONTEXT_VARIABLE_LINK("link"),
    EMAIL_CONTEXT_VARIABLE_CONTENT("content"),
    EMAIL_CONTEXT_VARIABLE_TITLE("title"),
    EMAIL_CONTEXT_VARIABLE_APPLICATION_NAME("applicationName");

    private final String value;

    EmailTemplateConstans(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
