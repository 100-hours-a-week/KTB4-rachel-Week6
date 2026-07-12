package kr.adapterz.jpa_practice.dto.like;

import lombok.Getter;
import lombok.NoArgsConstructor;
import kr.adapterz.jpa_practice.entity.PostInfo;
import kr.adapterz.jpa_practice.entity.Like;
import kr.adapterz.jpa_practice.entity.Post;

@Getter
@NoArgsConstructor
public class GetLikeInfoResponseDto {

    private int likeNum;
    private Long postId;


    public GetLikeInfoResponseDto(Post post) {
        this.likeNum = post.getPostInfo().getLikeCount(); // 좋아요 개수
        this.postId = post.getPostId();
    }

}
