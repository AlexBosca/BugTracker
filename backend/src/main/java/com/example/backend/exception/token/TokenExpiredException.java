package com.example.backend.exception.token;

import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.TOKEN_EXPIRED;

public class TokenExpiredException extends BaseRuntimeException {

    public TokenExpiredException() {
        super(TOKEN_EXPIRED);
    }
}
