package kr.adapterz.jpa_practice.dto.comment;

import kr.adapterz.jpa_practice.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteResponseDto {
    private Long commentId;
    private Long postId;
    private Long userId;
    private int commentNum;

    public CommentDeleteResponseDto(Comment comment, int commentNum) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPost().getPostId();
        this.userId = comment.getAuthor().getUserId();
        this.commentNum = commentNum;
    }
}
