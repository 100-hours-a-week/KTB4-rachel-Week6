package kr.adapterz.jpa_practice.response;

import kr.adapterz.jpa_practice.dto.error.ErrorResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final String message;
    private final T data;
    private final List<ErrorResponseDto> error; // TODO: 이렇게 맞나

    // 성공용 팩토리 매서드
    public static <T> ApiResponse<T> of(String message, T data) {
        return new ApiResponse<>(message ,data, null);
    }

    // 예외 팩터리 메서드
    public static <T> ApiResponse<T> of(String message, T data, List<ErrorResponseDto> error) {
        return new ApiResponse<>(message, data, error);
    }
}
