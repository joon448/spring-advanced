package org.example.expert.domain.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "삭제되었거나 존재하지 않는 댓글입니다."),
    COMMENT_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "COMMENT-002", "요청 값이 유효하지 않습니다."),
    COMMENT_UNAUTHORIZED(HttpStatus.FORBIDDEN, "COMMENT-003", "권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
