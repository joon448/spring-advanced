package org.example.expert.domain.user.enums;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.common.exception.errorcode.UserErrorCode;

import java.util.Arrays;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(UserErrorCode.INVALID_USERROLE));
    }
}
