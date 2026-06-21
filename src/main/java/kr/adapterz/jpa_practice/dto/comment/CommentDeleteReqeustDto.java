package kr.adapterz.jpa_practice.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteReqeustDto {

    @NotNull(message = "user id가 비었습니다.")
    private Long userId;
}
