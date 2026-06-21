package kr.adapterz.jpa_practice.dto.user;

import kr.adapterz.jpa_practice.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto { // 회원가입 시 DTO
    private Long userId;


    public UserResponseDto(User user) {
        this.userId = user.getUserId();
    }
}
