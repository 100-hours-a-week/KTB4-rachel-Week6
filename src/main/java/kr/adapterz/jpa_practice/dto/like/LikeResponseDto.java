package kr.adapterz.jpa_practice.dto.like;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeResponseDto {

    private boolean isLike;

    private Long userId;

    private LikeInfo likeInfo;

    public LikeResponseDto(boolean isLike, Long userId, Long postId, int likeNum) {
        this.isLike = isLike;
        this.userId = userId;
        this.likeInfo = new LikeInfo(likeNum, postId);
    }

    @Getter
    public static class LikeInfo {
        private final int likeNum;
        private final Long postId;

        public LikeInfo(int likeNum, Long postId) {
            this.likeNum = likeNum;
            this.postId = postId;
        }
    }
}