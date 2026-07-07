package kr.adapterz.jpa_practice.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRequestDto {
    @NotBlank(message = "만료된 Access Token은 필수입니다.")
    private String accessToken;

    @NotBlank(message = "재발급을 위한 Refresh Token은 필수입니다.")
    private String refreshToken;
}
