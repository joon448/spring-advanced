package org.example.expert.domain.common.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WeatherErrorCode implements ErrorCode {
    WEATHER_NOT_FOUND(HttpStatus.NOT_FOUND, "WEATHER-001", "날씨 데이터가 없습니다."),
    TODAY_WEATHER_NOT_FOUND(HttpStatus.NOT_FOUND, "WEATHER-002", "오늘에 해당하는 날씨 데이터를 찾을 수 없습니다."),
    FAIL_TO_GET_WEATHER(HttpStatus.FORBIDDEN, "WEATHER-003", "날씨 데이털르 가져오는 데 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
