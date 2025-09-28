package ro.alexportfolio.backend.util;

public class UserIdGenerator {

    private UserIdGenerator() {
        throw new IllegalStateException("Utility class");
    }

    public static String generateUserId(final String firstName,
                                        final String lastName) {
        String firstPart = firstName.substring(0, 2).toLowerCase();
        String lastPart = lastName.substring(0, 2).toLowerCase();
        String randomPart = generateRandomString(4);
        return firstPart + lastPart + randomPart;
    }

    @SuppressWarnings("java:S2245") // Not security-sensitive: random user IDs are cosmetic only
    private static String generateRandomString(final int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
}
