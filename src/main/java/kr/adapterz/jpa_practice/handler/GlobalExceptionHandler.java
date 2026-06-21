package kr.adapterz.jpa_practice.handler;

import kr.adapterz.jpa_practice.dto.error.ErrorResponseDto;
import kr.adapterz.jpa_practice.exception.BusinessException;
import kr.adapterz.jpa_practice.exception.NotFoundException;
import kr.adapterz.jpa_practice.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(
            NotFoundException exception
    ) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(ApiResponse.of(exception.getCode(), null));
    }

    // 비즈니스 로직에서 걸린 예외 메서드
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(
            BusinessException exception
    ) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(ApiResponse.of(exception.getCode(), null, null)); // TODO: getField 해야하는데
    }

    // TODO: List에 넣는게 Dto를 넣는게 아닌 것 같은데 차선책이 뭔지 모름
    // Valid 검사에 맞지 않을 때 메서드
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        List<ErrorResponseDto> errorDetails = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorResponseDto(
                        fieldError.getField(),
                        fieldError.getCode().toLowerCase(),
                        fieldError.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 무조건 400 Bad Request
                .body(ApiResponse.of(
                        "invalid_request", // message 자리 고정
                        null,              // data 자리 (null)
                        errorDetails       // error 자리에 방금 만든 리스트 주입
                        ));
    }

}
