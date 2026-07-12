package kr.adapterz.jpa_practice.dto.comment;

import kr.adapterz.jpa_practice.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentCreateResponseDto {
    private final Long commentId;
    private final LocalDateTime createdAt;
    private final String nickname;

    public CommentCreateResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.createdAt = comment.getCreatedAt();
        this.nickname = comment.getAuthor().getNickname();
    }
}