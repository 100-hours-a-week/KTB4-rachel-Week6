package kr.adapterz.jpa_practice.service;

import kr.adapterz.jpa_practice.dto.post.*;
import kr.adapterz.jpa_practice.dto.user.UserResponseDto;
import kr.adapterz.jpa_practice.entity.Comment;
import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.PostImage;
import kr.adapterz.jpa_practice.entity.User;
import kr.adapterz.jpa_practice.entity.Like;
import kr.adapterz.jpa_practice.exception.*;
import kr.adapterz.jpa_practice.repository.CommentRepository;
import kr.adapterz.jpa_practice.repository.LikeRepository;
import kr.adapterz.jpa_practice.repository.PostRepository;
import kr.adapterz.jpa_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public PostResponseDto createPost(Long userId, PostRequestDto request) { // TODO: 언제 userId를 주고 받는걸까

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND"));

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                author
        );

        // 게시글에 이미지를 첨부하였을 경우
        if(!request.getImages().isEmpty()) { //TODO: 개별 이미지 당 null 확인해줘야함
            for (String url: request.getImages())
            {
                if(url != null) post.addPostImage(url);
            }
        }

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }



    public PostUpdateResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        post.checkAndUpdateNickname();

        return new PostUpdateResponseDto(post);
    }


    @Transactional
    public List<PostUpdateResponseDto> getAllPost() {
        List<Post> posts = postRepository.findAll(); // TODO: 전체 조회

        return posts.stream()
                .peek(post -> post.checkAndUpdateNickname())
                .map(post -> new PostUpdateResponseDto(post))
                .collect(Collectors.toList());
    }


    @Transactional
    public PostUpdateResponseDto updatePost(Long postId, PostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));


        // 제목, 내용 업데이트할때 null 값 확인하는 코드 추가
        if (request.getTitle() != null) post.changeTitle(request.getTitle());
        if (request.getContent() != null) post.changeContent(request.getContent());

//        if(!request.getImages().isEmpty()) // 특정 원하는 이미지만 어떻게 수정해여하지
//        {
//
//        }



        return new PostUpdateResponseDto(post);
    }


    @Transactional
    public void deletePost(Long postId) {

        // 먼저 Post 자체가 있는지 확인하는 구문 추가
        Post post = postRepository.findById(postId)
                        .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));

        // 연관된 댓글과 좋아요도 지우기
        if (post.getComments() != null) // 리스트가 비어있지않으면
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
