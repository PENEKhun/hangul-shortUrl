package com.penek.shortUrl.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_INPUT(BAD_REQUEST, "invaild input"),
    NO_INPUT(BAD_REQUEST, "no input"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_PERMISSION(UNAUTHORIZED, "No Permission"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    LINK_NOT_FOUND(NOT_FOUND, "link not found"),
    RESOURCE_NOT_FOUND(NOT_FOUND, "resource not found"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "resource is exist"),

    /* 500 Internal Server : 서버 처리 실패 오류 */
    MAKE_FAIL(INTERNAL_SERVER_ERROR, "make url failed"),
    CAPTCHA_ERROR(INTERNAL_SERVER_ERROR, "captcha error"),
    DATE_FORMATTING_ERROR(INTERNAL_SERVER_ERROR, "date formatting error"),
    IP_DB_NOT_FOUND(INTERNAL_SERVER_ERROR, "IP_DB_NOT_FOUND"),

    //보안 위배되는 url
    REDIRECT_URL(CONFLICT, "redirect URL"), //401 리다이렉션되는 url을 넣을때
    BAD_SECURITY_URL(CONFLICT, "bad security URL"), //402 짧링 링크를 넣을때


    ;

    private final HttpStatus httpStatus;
    private final String detail;
}
