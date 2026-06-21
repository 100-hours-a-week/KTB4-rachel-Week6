package kr.adapterz.jpa_practice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException{
    private final String code;
    private final HttpStatus status; // HttpStatus 라는 데이터타입: 열거형(Enum, 이넘) 데이터 타입

    public BusinessException(String code, HttpStatus status) {
        super(code);
        this.code = code;
        this.status = status;
    }
}
