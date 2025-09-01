package org.example.expert.domain.auth.exception;

import org.example.expert.domain.common.exception.GlobalException;
import org.example.expert.domain.common.exception.errorcode.ErrorCode;

public class AuthException extends GlobalException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
