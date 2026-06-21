package kr.adapterz.jpa_practice.service;

import kr.adadpterz.springboot_project.dto.comment.*;
import kr.adadpterz.springboot_project.entity.Comment;
import kr.adadpterz.springboot_project.entity.Post;
import kr.adadpterz.springboot_project.entity.User;
import kr.adadpterz.springboot_project.exception.NotFoundException;
import kr.adadpterz.springboot_project.repository.CommentRepository;
import kr.adadpterz.springboot_project.repository.PostRepository;
import kr.adadpterz.springboot_project.repository.UserRepository;
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

        // TODO: 여기서 postId를 검사할필요없긴하다.. postId가 없다면 controller 계층에서 이미 405 그런 메소드없다고 나올 것임 -> postId가 있다고 가정하고 조회
        // List에 엔터티 Comment로 넣고 findall
        List<Comment> comments = commentRepository.findAll(postId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        List<CommentResponseDto> dtoList = comments.stream()
                .map(CommentResponseDto::new)
                .toList();

        return new CommentListResponseDto(dtoList, postId);
    }

    public CommentCreateResponseDto createComment(Long postId, CommentRequestDto request) {

        // 1. 컨트롤러 계층에서 postId가 있어야 service로 넘어오겟지만 한번더 검사
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("POST_NOT_FOUND")); // TODO: 예외 맞는지 검사

        // 2. 가입한 유저가 댓글을 작성하는지 확인
        User author = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("")); // TODO: 예외 맞는지 검사

        Comment comment = new Comment(
                post,
                author,
                request.getCommentContent()
        );

        Comment savedComment = commentRepository.save(postId, comment);
        return new CommentCreateResponseDto(savedComment);
    }


    // updateComment
    public CommentUpdateResponseDto updateComment(Long postId, Long commentId, CommentRequestDto request) {
        Comment comment = commentRepository.findById(postId, commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        // 본인이 작성한 댓글만 업데이트 가능
        if (!comment.getUser().getId().equals(request.getUserId())) {
            throw new IllegalArgumentException("NOT_AUTHORIZED_COMMENT_OWNER");
        }

        comment.changeContent(request.getCommentContent());

        return new CommentUpdateResponseDto(comment);
    }


    //deleteComment
    public CommentDeleteResponseDto deleteComment(Long postId, Long commentId, CommentDeleteReqeustDto request) {

        // 글이 있는지 확인
        Comment comment = commentRepository.findById(postId, commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));
//        // 본인이 생성한 댓글만 삭제할 수 있음
//        Comment comment = commentRepository.findById() // 아 Comment 레포지토리 다시 만들어야 하나. userId를 포함한 database가 아님..
//
//        if (request.getUserId().equals(...)){
//            commentRepository.deleteComment(postId, commentId);
//        }
        if (!comment.getUser().getId().equals(request.getUserId())) { // 재미나이한테 도움받은 부분. 엔터티에서 getUser하고 getId 한 부분이 어설펐었음
            throw new IllegalArgumentException("NOT_AUTHORIZED_COMMENT_OWNER");
        }

        Comment deletedComment = commentRepository.deleteComment(postId, commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));

        int remainCount = commentRepository.findAll(postId)
                .map(List::size)
                .orElse(0);

        return new CommentDeleteResponseDto(deletedComment, remainCount);
    }
}
