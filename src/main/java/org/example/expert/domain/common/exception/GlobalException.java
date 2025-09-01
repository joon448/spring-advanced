package org.example.expert.domain.common.exception;

import lombok.Getter;
import org.example.expert.domain.common.exception.errorcode.ErrorCode;

@Getter
public class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;
    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}