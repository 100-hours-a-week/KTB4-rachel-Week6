package kr.adapterz.jpa_practice.dto.auth;
import kr.adapterz.jpa_practice.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String grantType; // JWT 권한 인증 타입(Bearer등)
    private String accessToken; // 우리가 만든 진짜 토큰
    private String refreshToken; // 추후 구현 예정

    private UserResponseDto userInfo;
}
