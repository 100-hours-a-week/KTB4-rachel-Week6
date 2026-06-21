//package kr.adapterz.jpa_practice.dto.like;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import kr.adapterz.jpa_practice.entity.PostInfo;
//
//@Getter
//@NoArgsConstructor
//public class LikeGetResponseDto {
//    private LikeInfo likeInfo;
//
//    public LikeGetResponseDto(Long postId, int likeNum) {
//        this.likeInfo = new LikeInfo(likeNum, postId);
//    }
//
//    @Getter
//    public static class LikeInfo {
//        private int likeNum;
//        private Long postId;
//
//        public LikeInfo(PostInfo postInfo, int likeNum, Long postId) {
//            this.likeNum = postInfo.getLikeCount();
//            this.postId = postInfo.getPost().getPostId();
//        }
//    }
//}