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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
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


    // getAllComment
    @Transactional
    public CommentListResponseDto getAllComment(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUNT"));

        List<Comment> comments = post.getComments();

        List<CommentResponseDto> dtoList = comments.stream()
                .peek(comment -> comment.checkAndUpdateNickname())
                .map(comment -> new CommentResponseDto(comment))
                .toList();

        return new CommentListResponseDto(dtoList, postId);
    }


    // updateComment
    @Transactional
    public CommentUpdateResponseDto updateComment(Long postId, Long commentId, CommentRequestDto request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("COMMENT_NOT_FOUND"));


        // 본인이 작성한 댓글만 업데이트 가능
        if (!comment.getAuthor().getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("NOT_AUTHORIZED_COMMENT_OWNER");
        }


        // null 값 확인하고 업데이트 - 근데 if문 true일때만 업데이트 되는데 이럼?
        if(request.getCommentContent() != null)
        {
            comment.checkAndUpdateNickname();
            comment.changeContent(request.getCommentContent());
        }

        return new CommentUpdateResponseDto(comment);
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
