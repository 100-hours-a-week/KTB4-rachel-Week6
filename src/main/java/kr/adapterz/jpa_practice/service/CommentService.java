package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.comment.*;
import kr.adapterz.jpa_practice.entity.Comment;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.exception.NotFoundException;
import kr.adapterz.jpa_practice.repository.CommentRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // getAllComment
    public CommentListResponseDto getAllComment(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUNT"));

        List<Comment> comments = post.getComments(); // 비어있으면?

        List<CommentResponseDto> dtoList = comments.stream()
                .map(CommentResponseDto::new)
                .toList();

        return new CommentListResponseDto(dtoList, postId);
    }

    public CommentCreateResponseDto createComment(Long postId, CommentRequestDto request) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Comment comment = new Comment(
                request.getCommentContent(),
                author,
                post
        );

        Comment savedComment = commentRepository.save(comment);
        return new CommentCreateResponseDto(savedComment);
    }


    // updateComment
    public CommentUpdateResponseDto updateComment(Long postId, Long commentId, CommentRequestDto request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        // TODO: comment 레포지토리로 확인하는 것과 아래 코드랑 비교해봐야함
//        if (!comment.getPost().getPostId().equals(postId)) {
//            throw new NotFoundException("COMMENT_NOT_FOUND");
//        }

        // 본인이 작성한 댓글만 업데이트 가능
        if (!comment.getAuthor().getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("NOT_AUTHORIZED_COMMENT_OWNER");
        }

        // null 값 확인
        if(request.getCommentContent() != null)
        {
            comment.changeContent(request.getCommentContent());
        }

        return new CommentUpdateResponseDto(comment); // TODO: 성공시만 가능인데 reutrn이 되나?
    }


    //deleteComment
    public CommentDeleteResponseDto deleteComment(Long postId, Long commentId, CommentDeleteReqeustDto request) {

        // 글이 있는지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        // 본인이 생성한 댓글만 삭제할 수 있음
        if (!comment.getAuthor().getUserId().equals(request.getUserId()))
        {
            throw new IllegalArgumentException("NOT_AUTHORIZED_COMMENT_OWNER");
        }

        commentRepository.delete(comment);

        return new CommentDeleteResponseDto(comment); // 삭제했는데 dto 객체로 보내져?
    }
}
