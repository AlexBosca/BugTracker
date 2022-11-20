package com.example.backend.exception.registration;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.EMAIL_SEND_FAIL;

public class EmailSendFailException extends BaseRuntimeException {

    public EmailSendFailException() {
        super(EMAIL_SEND_FAIL);
    }
}
