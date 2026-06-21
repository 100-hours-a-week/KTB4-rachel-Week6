package kr.adapterz.jpa_practice.controller;

import jakarta.validation.Valid;
import kr.adapterz.jpa_practice.dto.post.PostRequestDto;
import kr.adapterz.jpa_practice.dto.post.PostResponseDto;
import kr.adapterz.jpa_practice.dto.post.PostUpdateResponseDto;
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
    public ResponseEntity<ApiResponse<List<PostUpdateResponseDto>>> getAllPost () {
        List<PostUpdateResponseDto> result = postService.getAllPost(); // TODO: 전체 조회
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("POSTS_RETRIEVED",result, null));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostUpdateResponseDto>> getPost (
            @PathVariable Long postId
    ) {
        PostUpdateResponseDto result = postService.getPost(postId);
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

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostUpdateResponseDto>> updatePost (
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto request
    ) {
        PostUpdateResponseDto result = postService.updatePost(postId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("POST_UPDATED", result));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("POST_DELETED", null));
    }
}
