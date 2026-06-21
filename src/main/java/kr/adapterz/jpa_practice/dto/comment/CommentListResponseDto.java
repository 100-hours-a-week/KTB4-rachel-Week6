package kr.adapterz.jpa_practice.dto.comment;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentListResponseDto {
    private final List<CommentResponseDto> comments;
    private final CommentInfo commentInfo;

    public CommentListResponseDto(List<CommentResponseDto> comments, Long postId) {
        this.comments = comments;
        this.commentInfo = new CommentInfo(comments.size(), postId);
    }

    @Getter
    public static class CommentInfo {
        private final int commentNum;
        private final Long postId;

        public CommentInfo(int commentNum, Long postId) {
            this.commentNum = commentNum;
            this.postId = postId;
        }
    }
}