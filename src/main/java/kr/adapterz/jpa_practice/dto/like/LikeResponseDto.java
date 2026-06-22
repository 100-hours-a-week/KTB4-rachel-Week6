package kr.adapterz.jpa_practice.dto.like;

import lombok.Getter;
import lombok.NoArgsConstructor;
import kr.adapterz.jpa_practice.entity.PostInfo;
import kr.adapterz.jpa_practice.entity.Like;
import kr.adapterz.jpa_practice.entity.Post;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private boolean isLike;
    private Long userId;
    private LikeInfo likeInfo;

    public LikeResponseDto(boolean isLike, Post post) {
        this.isLike = isLike;
        this.userId = post.getAuthor().getUserId();
        this.likeInfo = new LikeInfo(post);
    }

    @Getter
    public static class LikeInfo {
        private final int likeNum;
        private final Long postId;

        public LikeInfo(Post post) {
            this.likeNum = post.getPostInfo().getLikeCount();
            this.postId = post.getPostId();
        }
    }
}