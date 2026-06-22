package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.like.*;
import kr.adapterz.jpa_practice.entity.LikeId;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.entity.Like;
import kr.adapterz.jpa_practice.exception.NotFoundException;
import kr.adapterz.jpa_practice.repository.LikeRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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

        // 회원가입한 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        // request.isLike()가 true일 때만 저장소에 반영
        if (request.isLike()) {
            Like like = new Like(
                    true,
                    user,
                    post
            );

            likeRepository.save(like);
        }

        return new LikeResponseDto(true, post);
    }

    // 3. 좋아요 취소
    public LikeResponseDto cancelLike(Long postId, Long userId) {

        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        // 회원가입한 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        // 좋아요 엔티티가 있는지 확인
        LikeId likeId = new LikeId(postId, userId); // 오직 좋아요 엔티티 확인을 위한 '확인용 좋아요 식별자 클래스'..

        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new NotFoundException("NEVER_PRESS_LIKEBUTTON"));

        likeRepository.delete(like);

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