package org.example.expert.domain.common.exception;

import org.example.expert.domain.common.exception.errorcode.ErrorCode;

public class ServerException extends GlobalException {
    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
