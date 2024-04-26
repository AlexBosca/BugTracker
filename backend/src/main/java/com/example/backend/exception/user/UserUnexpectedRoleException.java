package com.example.backend.exception.user;

import static com.example.backend.util.ExceptionUtilities.USER_ROLE_MISMATCH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.backend.exception.BaseRuntimeException;

@ResponseStatus(value = BAD_REQUEST)
public class UserUnexpectedRoleException extends BaseRuntimeException {
    
    public UserUnexpectedRoleException(String userId, String actualRole, String expectedRole) {
        super(USER_ROLE_MISMATCH, userId, actualRole, expectedRole);
    }
}
