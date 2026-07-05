package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.like.*;
import kr.adapterz.jpa_practice.entity.*;
import kr.adapterz.jpa_practice.exception.NotFoundException;
import kr.adapterz.jpa_practice.repository.LikeRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 1. 좋아요 정보 조회
    public GetLikeInfoResponseDto getLikeInfo(Long postId) {

        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));


        // 좋아요 정보 조회를 위한 dto 다시 생성함

        return new GetLikeInfoResponseDto(post);
    }

    // 2. 좋아요 누르기
    public LikeResponseDto pressLike(Long postId, Long userId, LikeRequestDto request) {

        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        // 로그인 한 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));


        // 로그인 한 유저가 좋아요를 이미 눌렀는지 아닌지 확인 - LikeId에서 해당 postId와 userId가 이미 존재하는가?
        // request.isLike()가 true일 때만 저장소에 반영 // false면 반영 안함
        if (request.isLike()) {
            LikeId likeId = new LikeId(postId, userId);
            Like like = likeRepository.findById(likeId) // 확인을 위해 임시로 new 생성자를 이용해 만들어보는건데..이러면 데이터가 저장되는거임 안되는거임?
                    .orElseGet(() -> { //findById에서 실패하여 Optional을 반환 시 들어옴.

                        return new Like(true, user, post);
                    });
            likeRepository.save(like); // 이거를 orElseGet 밖에다 쓰면 likeRepository에 likeId가 이미 있다면 중복으로 like가 저장되지 않음. JPA에서 중복 저장을 막으니까.

        }

        post.getPostInfo().increaseLikeCount(); // 좋아요 카운트 +1


//        if (request.isLike()) {
//            Like like = new Like(
//                    true,
//                    user,
//                    post
//            );
//
//            likeRepository.save(like);
//        }

        return new LikeResponseDto(true, post);
    }

    // 3. 좋아요 취소
    public LikeResponseDto cancelLike(Long postId, Long userId, LikeRequestDto request) {

        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));


        // 좋아요 엔티티가 있는지 확인
        if (!request.isLike()) {
            LikeId likeId = new LikeId(postId, userId);
            Like like = likeRepository.findById(likeId)
                    .orElseThrow(() -> new NotFoundException("NEVER_PRESS_LIKEBUTTON"));

            likeRepository.delete(like);
            post.getPostInfo().decreaseLikeCount();
        }
//        LikeId likeId = new LikeId(postId, userId); // 오직 좋아요 엔티티 확인을 위한 '확인용 좋아요 식별자 클래스'..
//
//        Like like = likeRepository.findById(likeId)
//                .orElseThrow(() -> new NotFoundException("NEVER_PRESS_LIKEBUTTON"));


        return new LikeResponseDto(false, post);
    }

//    // 공통 검증 메서드
//    private void validatePostAndUser(Long postId, Long userId) {
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
//
//        User user = userRepository.findById(userId) // 소프트 삭제 시, userId == null인데, 이때는 예외 -> 문제없음
//                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));
//
//    }
}