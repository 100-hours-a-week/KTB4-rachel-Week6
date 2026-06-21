package kr.adapterz.jpa_practice.dto.user;

import kr.adapterz.jpa_practice.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class UserUpdateResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;

    public UserUpdateResponseDto(User user){
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
