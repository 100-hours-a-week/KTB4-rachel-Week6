package kr.adapterz.jpa_practice.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용이 비었습니다.")
    private String commentContent;;

    @NotNull(message = "user id가 비었습니다.")
    private Long userId;
}
