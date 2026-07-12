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
        if (request.isLike()) {
            LikeId likeId = new LikeId(postId, userId);

            likeRepository.findById(likeId).ifPresent(like -> {
                throw new NotFoundException("ALREADY_PRESSED_LIKE"); //TODO: 예외처리 다른걸로 하기
            });
        }

        Like like = new Like(true, user, post);
        likeRepository.save(like);
        post.getPostInfo().increaseLikeCount();

        return new LikeResponseDto(true, post);
    }

    // 3. 좋아요 취소
    public LikeResponseDto cancelLike(Long postId, Long userId) {

        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        // 로그인 한 유저 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));


        LikeId likeId = new LikeId(postId, userId);

        likeRepository.findById(likeId).ifPresent(like -> {
            likeRepository.delete(like);
            post.getPostInfo().decreaseLikeCount();
        });



        return new LikeResponseDto(false, post);
    }

}