package kr.adapterz.jpa_practice.dto.comment;


import kr.adapterz.jpa_practice.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentUpdateResponseDto {
    private Long commentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentUpdateResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = LocalDateTime.now();
    }
}
