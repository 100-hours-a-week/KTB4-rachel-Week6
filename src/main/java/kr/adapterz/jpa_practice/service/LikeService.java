package kr.adapterz.jpa_practice.service;

import kr.adadpterz.springboot_project.dto.like.*;
import kr.adadpterz.springboot_project.exception.NotFoundException;
import kr.adadpterz.springboot_project.repository.LikeRepository;
import kr.adadpterz.springboot_project.repository.PostRepository;
import kr.adadpterz.springboot_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 1. 좋아요 정보 조회
    public LikeGetResponseDto getLikeInfo(Long postId) {
        validatePostAndUser(postId, null); // 게시글 존재 체크만 수행
        int count = likeRepository.getLikeCount(postId);
        return new LikeGetResponseDto(postId, count);
    }

    // 2. 좋아요 누르기
    public LikeResponseDto pressLike(Long postId, Long userId, LikeRequestDto request) {
        validatePostAndUser(postId, userId);

        // request.isLike()가 true일 때만 저장소에 반영
        if (request.isLike()) {
            likeRepository.addLike(postId, userId);
        }

        int count = likeRepository.getLikeCount(postId);
        return new LikeResponseDto(true, userId, postId, count);
    }

    // 3. 좋아요 취소
    public LikeResponseDto cancelLike(Long postId, Long userId) {
        validatePostAndUser(postId, userId);

        likeRepository.removeLike(postId, userId);
        int count = likeRepository.getLikeCount(postId);

        return new LikeResponseDto(false, userId, postId, count);
    }

    // 공통 검증 메서드
    private void validatePostAndUser(Long postId, Long userId) {
        if (!postRepository.findById(postId).isPresent()) {
            throw new NotFoundException("POST_NOT_FOUND");
        }
        if (userId != null && !userRepository.findById(userId).isPresent()) {
            throw new NotFoundException("USER_NOT_FOUND");
        }
    }
}