package org.example.expert.domain.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode {
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO-001", "Todo not found"),
    TODO_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "TODO-002", "요청 값이 유효하지 않습니다."),
    TODO_UNAUTHORIZED(HttpStatus.FORBIDDEN, "TODO-003", "권한이 없습니다.");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
