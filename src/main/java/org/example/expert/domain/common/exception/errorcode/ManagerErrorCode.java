package org.example.expert.domain.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ManagerErrorCode implements ErrorCode {
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "MANAGER=001", "Manager not found"),
    MANAGER_NOT_OWNER(HttpStatus.FORBIDDEN, "MANAGER-002", "일정을 생성한 유저만 담당자를 지정할 수 있습니다."),
    MANAGER_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "MANAGER-003", "등록하려고 하는 담당자 유저가 존재하지 않습니다."),
    MANAGER_CANNOT_ASSIGN_SELF(HttpStatus.BAD_REQUEST, "MANAGER-004", "일정 작성자는 본인을 담당자로 등록할 수 없습니다."),
    MANAGER_CREATOR_INVALID(HttpStatus.NOT_FOUND, "MANAGER-005", "해당 일정을 만든 유저가 유효하지 않습니다."),
    MANAGER_NOT_ASSIGNED(HttpStatus.FORBIDDEN, "MANAGER-006", "해당 일정에 등록된 담당자가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
