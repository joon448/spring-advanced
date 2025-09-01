package org.example.expert.domain.common.exception;

import org.example.expert.domain.common.exception.errorcode.ErrorCode;

public class InvalidRequestException extends GlobalException {
    public InvalidRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
