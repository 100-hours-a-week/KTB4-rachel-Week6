package kr.adapterz.jpa_practice.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String code) {
        super(code, HttpStatus.FORBIDDEN); // 403 Forbidden 상태코드 부여
    }
}