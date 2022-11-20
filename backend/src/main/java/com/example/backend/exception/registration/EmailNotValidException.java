package com.example.backend.exception.registration;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.EMAIL_NOT_VALID;

public class EmailNotValidException extends BaseRuntimeException {

    public EmailNotValidException() {
        super(EMAIL_NOT_VALID);
    }
}
