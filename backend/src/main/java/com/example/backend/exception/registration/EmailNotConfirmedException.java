package com.example.backend.exception.registration;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.EMAIL_NOT_CONFIRMED;

public class EmailNotConfirmedException extends BaseRuntimeException {
    
    public EmailNotConfirmedException() {
        super(EMAIL_NOT_CONFIRMED);
    }
}
