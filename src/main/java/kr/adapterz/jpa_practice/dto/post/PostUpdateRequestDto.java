//package kr.adapterz.jpa_practice.dto.post;
//
//import kr.adapterz.jpa_practice.dto.comment.CommentResponseDto;
//import kr.adapterz.jpa_practice.entity.Post;
//import kr.adapterz.jpa_practice.entity.PostImage;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@NoArgsConstructor
//public class PostUpdateRequestDto {
//    private Long postId;
//
//    private String title;
//    private String content;
//    private List<String> images; // 특정 몇번째 이미지인지 어케 알지..
//    private Long authorId;
//    private String nickname;
//    private int likeCount; // 좋아요수
//    private int commentCount; // 댓글수
//    // private int viewCount; // 조회수
//
//    private List<CommentResponseDto> comments;
//
//    private LocalDateTime updatedAt;
//
//
//    public PostUpdateRequestDto(Post post) {
//        this.postId = post.getPostId();
//        this.title = post.getTitle();
//        this.authorId = post.getAuthor().getUserId();
//        this.nickname = post.getAuthor().getNickname();
//        this.content = post.getContent();
//
//        this.images = post.getPostImages().stream()
//                .map(PostImage::getContentImage)
//                .collect(Collectors.toList());
//
//        this.likeCount = post.getPostInfo().getLikeCount();
//        this.commentCount = post.getPostInfo().getCommentCount();
//        // this.viewCount = post.getViewCount();
//
//        this.updatedAt = post.getUpdatedAt();
//
//        this.comments = post.getComments().stream()
//                .map(comment -> new CommentResponseDto(comment))
//                .collect(Collectors.toList());
//
//    }
//}
//
//
//
