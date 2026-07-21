package kr.adapterz.jpa_practice.service;

import jakarta.validation.constraints.Positive;
import kr.adapterz.jpa_practice.dto.post.*;
import kr.adapterz.jpa_practice.dto.user.UserResponseDto;
import kr.adapterz.jpa_practice.entity.*;
import kr.adapterz.jpa_practice.exception.*;
import kr.adapterz.jpa_practice.repository.CommentRepository;
import kr.adapterz.jpa_practice.repository.LikeRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public PostResponseDto createPost(Long userId, PostRequestDto request) {

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                author
        );

        // 게시글에 이미지를 첨부하였을 경우
        if(!request.getImages().isEmpty()) {
            for (String url: request.getImages())
            {
                if(url != null) post.addPostImage(url);
            }
        }

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        post.checkAndUpdateNickname();

        return new PostResponseDto(post);
    }

    @Transactional
    public List<AllPostsResponseDto> getAllPost(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        List<Post> posts = postPage.getContent();

        return posts.stream()
                .peek(post -> {
                    post.checkAndUpdateNickname();
                    System.out.println("postInfo 값: " + post.getPostInfo());
                    if(post.getPostInfo() == null) {
                        PostInfo info = new PostInfo(post);
                        post.LinkPostInfo(info);
                    }
                })
                .map(post -> new AllPostsResponseDto(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostUpdateResponseDto updatePost(Long postId, CustomUserDetails userDetails, PostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        // 작성자 검증
        if (!userDetails.getUserId().equals(post.getAuthor().getUserId())) {
            throw new AccessDeniedException("USER_MISMATCH");
        }

        // 제목, 내용 업데이트할때 null 값 확인하는 코드 추가
        if (request.getTitle() != null) post.changeTitle(request.getTitle());
        if (request.getContent() != null) post.changeContent(request.getContent());

        return new PostUpdateResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, CustomUserDetails userDetails) {

        // 먼저 Post 자체가 있는지 확인하는 구문 추가
        Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        // 현재 삭제를 시도한 유저가 작성자인지 체크
        if (!userDetails.getUserId().equals(post.getAuthor().getUserId())) {
            throw new AccessDeniedException("USER_MISMATCH");
        }

        // 연관된 댓글과 좋아요도 지우기
        if (post.getComments() != null)
        {
            for(Comment comment: post.getComments())
            {
                commentRepository.delete(comment);
            }
        }

        if(post.getLikes() != null)
        {
            for(Like like : post.getLikes())
            {
                likeRepository.delete(like);
            }
        }

        postRepository.delete(post);
    }
}
