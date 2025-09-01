package org.example.expert.domain.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    AUTH_INVALID_TYPE(HttpStatus.BAD_REQUEST, "AUTH-001", "@Auth와 AuthUser 타입은 함께 사용되어야 합니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-002", "Token Not Found");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
