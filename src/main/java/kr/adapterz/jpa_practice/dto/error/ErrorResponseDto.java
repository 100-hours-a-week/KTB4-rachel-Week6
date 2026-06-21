package kr.adapterz.jpa_practice.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private final String field;
    private final String code;

    @JsonProperty("error_message")
    private final String errorMessage;


    public ErrorResponseDto(String field, String code, String errorMessage) {
        this.field = field;
        this.code = code;
        this.errorMessage = errorMessage;
    }

//    public static ErrorResponseDto of(String code){
//        return new ErrorResponseDto(code);
//    }

}
