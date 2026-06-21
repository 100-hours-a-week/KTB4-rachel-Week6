package kr.adapterz.jpa_practice.controller;
import jakarta.validation.Valid;
import kr.adapterz.jpa_practice.dto.comment.*;
import kr.adapterz.jpa_practice.response.ApiResponse;
import kr.adapterz.jpa_practice.service.CommentService;
import kr.adapterz.jpa_practice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<CommentListResponseDto>> getAllComment (
            @PathVariable Long postId
    ) {
        CommentListResponseDto result = commentService.getAllComment(postId); // TODO: 전체 조회
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("COMMENTS_RETRIEVED",result, null));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CommentCreateResponseDto>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto request
    ) {
        CommentCreateResponseDto result = commentService.createComment(postId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("COMMENT_CREATED", result, null));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentUpdateResponseDto>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto request
    ) {
        CommentUpdateResponseDto result = commentService.updateComment(postId, commentId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("COMMENT_UPDATE", result, null));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentDeleteResponseDto>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDeleteReqeustDto request // 오타 수정 반영
    ) {
        CommentDeleteResponseDto result = commentService.deleteComment(postId, commentId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of("COMMENT_DELETE", result, null));
    }
}
