package kr.adapterz.jpa_practice.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends BusinessException {
    public DuplicateException(String code) {
        super(code, HttpStatus.CONFLICT);
    }
}
