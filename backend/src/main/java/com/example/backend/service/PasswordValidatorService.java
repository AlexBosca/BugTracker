package com.example.backend.service;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@Service
public class PasswordValidatorService implements Predicate<String> {

    private final static int MINIMUM_PASSWORD_LENGTH = 8;
    private final static String AT_LEAST_ONE_UPPER_CASE_CHAR_REGEX = "(.*[A-Z].*)";
    private final static String AT_LEAST_ONE_LOWER_CASE_CHAR_REGEX = "(.*[a-z].*)";
    private final static String AT_LEAST_ONE_DIGIT_REGEX = "(.*[0-9].*)";
    private final static String AT_LEAST_ONE_SPECIAL_CHAR_REGEX = "(.*[@#$%^&+=].*)";
    private final static String NO_WHITESPACE_REGEX = "(\\S+$)";

    @Override
    public boolean test(String password) {
        return isPasswordValid(password);
    }

    private boolean isPasswordValid(String password) {
        return  hasMinimumPasswordLength(password) &&
                containsAtLeastOneUpperCaseChar(password) &&
                containsAtLeastOneLowerCaseChar(password) &&
                containsAtLeastOneDigit(password) &&
                containsAtLeastOneSpecialChar(password) &&
                containsNoWhitespaces(password);
    }

    private boolean hasMinimumPasswordLength(String password) {
        int passwordLength = password.length();

        return passwordLength >= MINIMUM_PASSWORD_LENGTH;
    }

    private boolean containsAtLeastOneUpperCaseChar(String password) {
        Pattern pattern = compile(AT_LEAST_ONE_UPPER_CASE_CHAR_REGEX);

        return matchPassword(pattern, password);
    }

    private boolean containsAtLeastOneLowerCaseChar(String password) {
        Pattern pattern = compile(AT_LEAST_ONE_LOWER_CASE_CHAR_REGEX);

        return matchPassword(pattern, password);
    }

    private boolean containsAtLeastOneDigit(String password) {
        Pattern pattern = compile(AT_LEAST_ONE_DIGIT_REGEX);

        return matchPassword(pattern, password);
    }

    private boolean containsAtLeastOneSpecialChar(String password) {
        Pattern pattern = compile(AT_LEAST_ONE_SPECIAL_CHAR_REGEX);

        return matchPassword(pattern, password);
    }

    private boolean containsNoWhitespaces(String password) {
        Pattern pattern = compile(NO_WHITESPACE_REGEX);

        return matchPassword(pattern, password);
    }

    private boolean matchPassword(Pattern pattern, String password) {
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }
}
