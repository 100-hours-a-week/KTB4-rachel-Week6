package kr.adapterz.jpa_practice.dto.post;

import kr.adapterz.jpa_practice.dto.comment.CommentResponseDto;
import kr.adapterz.jpa_practice.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;

    private String title;
    private String content;
    private String image;
    private Long authorId; // 기존 명세서에서 수정함. author, nickname 없애고, userId를 authorId라 함
    private String nickname;
    // private int likeCount; // 좋아요수
    private int commentCount; // 댓글수
    private int viewCount; // 조회수

    private List<CommentResponseDto> comments;

    private LocalDateTime createdAt;



    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.authorId = post.getAuthor().getUserId();
        this.nickname = post.getAuthor().getNickname();
        this.content = post.getContent();
        // this.image = post.getContentImage();

        // this.likeCount = 0; // 좋아요도 따로 엔터티말들고 dto 등을 만들거임.
        // this.commentCount = post.getComments().size(); -> postInfo.getCommentCount()
        // this.viewCount = post.getViewCount();

        this.createdAt = post.getCreatedAt();

        this.comments = post.getComments().stream()
                .map(comment -> new CommentResponseDto(comment))
                .collect(Collectors.toList());

    }
}
