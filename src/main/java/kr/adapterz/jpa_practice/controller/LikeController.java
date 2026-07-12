package kr.adapterz.jpa_practice.controller;

import kr.adapterz.jpa_practice.dto.like.GetLikeInfoResponseDto;
import kr.adapterz.jpa_practice.dto.like.LikeResponseDto;
import kr.adapterz.jpa_practice.dto.like.LikeRequestDto;
import kr.adapterz.jpa_practice.dto.like.LikeResponseDto;
import kr.adapterz.jpa_practice.response.ApiResponse;
import kr.adapterz.jpa_practice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// @CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/posts/{postId}")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<GetLikeInfoResponseDto>> getLikes( // 특정 게시글에 대하여 좋아요 개수 조회
            @PathVariable Long postId
    ) {
        GetLikeInfoResponseDto result = likeService.getLikeInfo(postId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of("LIKE_RETRIVER", result, null));
    }



    @PostMapping("/{userId}/likes")
    public ResponseEntity<ApiResponse<LikeResponseDto>> createLike(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @RequestBody LikeRequestDto request
    ) {
        LikeResponseDto result = likeService.pressLike(postId, userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of("LIKE_CREATE", result, null));
    }

    @DeleteMapping("/{userId}/likes")
    public ResponseEntity<ApiResponse<LikeResponseDto>> deleteLike(
            @PathVariable Long postId,
            @PathVariable Long userId
    ) {
        LikeResponseDto result = likeService.cancelLike(postId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of("LIKE_DELETE", result, null));
    }
}
