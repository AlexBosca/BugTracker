package com.example.backend.exception.registration;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.EMAIL_CONFIRMED;

public class EmailAlreadyConfirmedException extends BaseRuntimeException {

    public EmailAlreadyConfirmedException() {
        super(EMAIL_CONFIRMED);
    }
}
