package kr.adapterz.jpa_practice.exception;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends BusinessException{
    public PasswordMismatchException(String code){
        super(code, HttpStatus.BAD_REQUEST);
    }
}
