package com.example.backend.exception.registration;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.EMAIL_TAKEN;

public class EmailTakenException extends BaseRuntimeException {

    public EmailTakenException() {
        super(EMAIL_TAKEN);
    }
}
