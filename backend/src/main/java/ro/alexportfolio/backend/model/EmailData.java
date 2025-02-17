package ro.alexportfolio.backend.model;

import java.util.Optional;

public record EmailData(String recipientName,
                        String recipientEmail,
                        String subject,
                        String title,
                        String applicationName,
                        Optional<String> confirmationLink,
                        Optional<String> notificationContent) {

    public static class Builder {
        private String recipientName;
        private String recipientEmail;
        private String subject;
        private String title;
        private String applicationName;
        private Optional<String> confirmationLink;
        private Optional<String> notificationContent;

        public Builder recipientName(String recipientName) {
            this.recipientName = recipientName;
            return this;
        }

        public Builder recipientEmail(String recipientEmail) {
            this.recipientEmail = recipientEmail;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder confirmationLink(Optional<String> confirmationLink) {
            this.confirmationLink = confirmationLink;
            return this;
        }

        public Builder notificationContent(Optional<String> notificationContent) {
            this.notificationContent = notificationContent;
            return this;
        }

        public EmailData build() {
            return new EmailData(recipientName, recipientEmail, subject, title, applicationName, confirmationLink,
                    notificationContent);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
