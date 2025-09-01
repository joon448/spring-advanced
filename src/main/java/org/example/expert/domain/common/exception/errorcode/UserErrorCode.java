package org.example.expert.domain.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "User not found"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "USER-002", "이미 존재하는 이메일입니다."),
    USER_NOT_REGISTERED(HttpStatus.NOT_FOUND, "USER-003", "가입되지 않은 유저입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "USER-004", "잘못된 비밀번호입니다."),
    INVALID_USERROLE(HttpStatus.BAD_REQUEST, "USER-005", "유효하지 않은 UserRole입니다."),
    PASSWORD_SAME_AS_OLD(HttpStatus.BAD_REQUEST, "USER-006", "새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
