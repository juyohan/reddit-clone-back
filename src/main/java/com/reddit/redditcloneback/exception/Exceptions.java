package com.reddit.redditcloneback.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum Exceptions {
    // user
    USER_NOT_FOUND(NOT_FOUND,"USER001", "사용자를 찾지 못했습니다."),
    USER_EXISTED(BAD_REQUEST, "USER002", "이미 존재한 사용자입니다."),
    USER_DISABLE(BAD_REQUEST, "USER003", "이메일 인증이 안된 사용자입니다."),
    USER_NOT_LOGIN(NOT_FOUND, "USER004", "로그인이 되어있지 않는 사용자입니다."),
    PERMISSION_DENIED(BAD_REQUEST, "USER005", "권한이 존재하지 않는 사용자입니다."),

    // redis
    CACHE_EXPIRED(NOT_FOUND, "REDIS001", "만료된 캐시입니다."),
    NO_CONNECTED(BAD_REQUEST, "REDIS002", "REDIS 에 연결을 실패하였습니다."),

    // 부가 에러
    NO_DATA(NOT_FOUND, "ERROR001", "null 값이 들어왔습니다."),
    AUTH_FAIL(UNAUTHORIZED, "ERROR002", "자격 증명하는데, 실패했습니다."),
    NOT_FOUND_PATH(BAD_REQUEST, "ERROR003", "잘못된 경로입니다."),

    // verification token
    VT_NOT_FOUND(NOT_FOUND, "VT001", "사용자와 일치되는 이메일 인증 토큰이 없습니다."),

    // mail
    MAIL_ERROR(BAD_REQUEST, "MAIL001", "메일 전송하는데 오류가 발생했습니다."),

    // feed
    FEED_NOT_FOUND(NOT_FOUND, "FEED001", "없는 피드입니다."),

    // Jwt
    JWT_WRONG_TYPE_TOKEN(BAD_REQUEST, "JWT001", "잘못된 JWT 의 서명입니다."),
    JWT_EXPIRED_TOKEN(BAD_REQUEST, "JWT002", "유효가 만료된 JWT 입니다."),
    JWT_UNSUPPORTED_TOKEN(BAD_REQUEST, "JWT003", "지원하지 않는 JWT 입니다."),
    JWT_WRONG_TOKEN(BAD_REQUEST, "JWT004", "옳바르지 않은 JWT 입니다."),
    JWT_NULL_TOKEN(NOT_FOUND, "JWT005", "토큰을 전달받지 못했습니다."),
    JWT_UNKNOWN_ERROR(BAD_REQUEST, "JWT006", "JWT 유효성 검사에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
