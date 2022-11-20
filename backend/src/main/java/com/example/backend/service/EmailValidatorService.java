package com.example.backend.service;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@Service
public class EmailValidatorService implements Predicate<String> {

    private final static String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean test(String email) {
        return isEmailValid(email);
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = compile(EMAIL_REGEX);

        return matchEmail(pattern, email);
    }

    private boolean matchEmail(Pattern pattern, String email) {
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
