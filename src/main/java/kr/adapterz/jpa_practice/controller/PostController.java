package kr.adapterz.jpa_practice.controller;

import jakarta.validation.Valid;
import kr.adapterz.jpa_practice.dto.post.PostRequestDto;
import kr.adapterz.jpa_practice.dto.post.PostResponseDto;
import kr.adapterz.jpa_practice.response.ApiResponse;
import kr.adapterz.jpa_practice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllPost () {
        List<PostResponseDto> result = postService.getAllPost(); // TODO: 전체 조회
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("POSTS_RETRIEVED",result, null));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPost (
            @PathVariable Long postId
    ) {
        PostResponseDto result = postService.getPost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Location", "/users" + result.getPostId())
                .body(ApiResponse.of("POST_RETRIEVED",result, null));
    }

    // TODO: post DTO 다시 만들어야 함.
    @PostMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @PathVariable Long userId,
            @Valid @RequestBody PostRequestDto request
    ) {
        PostResponseDto result = postService.createPost(userId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of("POST_CREATED", result));
    }

    @PatchMapping("/{postId}") //TODO: 수정 DTO 다시 만들어야함.
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost (
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto request
    ) {
        PostResponseDto result = postService.updatePost(postId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("POST_UPDATED", result));
    }

    @DeleteMapping("/{postId}") // TODO: post_id 나오도록 수정해야함. 삭제 DTO 자체를 다시 만들어야함
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("POST_DELETED", null));
    }
}
