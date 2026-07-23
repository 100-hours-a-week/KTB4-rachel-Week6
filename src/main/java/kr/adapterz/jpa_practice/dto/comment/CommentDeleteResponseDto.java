package kr.adapterz.jpa_practice.dto.comment;

import kr.adapterz.jpa_practice.entity.Comment;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.PostInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentDeleteResponseDto {
    private Long commentId;
    // private Long postId;
    // private Long userId;


    public CommentDeleteResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        // this.postId = comment.getPost().getPostId(); //TODO: 소프트삭제 안하면 NPE
        // this.userId = comment.getAuthor().getUserId();
    }
}
