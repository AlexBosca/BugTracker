package com.example.backend.exception.user;


import org.springframework.web.bind.annotation.ResponseStatus;
import com.example.backend.exception.BaseRuntimeException;

import static com.example.backend.util.ExceptionUtilities.ROLE_WITH_CODE_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND)
public class UserRoleNotFoundException extends BaseRuntimeException {
    
    public UserRoleNotFoundException(String code) {
        super(ROLE_WITH_CODE_NOT_FOUND, code);
    }
}
