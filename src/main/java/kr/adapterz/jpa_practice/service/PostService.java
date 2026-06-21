package kr.adapterz.jpa_practice.service;

import kr.adadpterz.springboot_project.dto.post.*;
import kr.adadpterz.springboot_project.entity.Post;
import kr.adadpterz.springboot_project.entity.User;
import kr.adadpterz.springboot_project.exception.*;
import kr.adadpterz.springboot_project.repository.PostRepository;
import kr.adadpterz.springboot_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public PostResponseDto createPost(Long userId, PostRequestDto request) { // TODO: 언제 userId를 주고 받는걸까

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                request.getImage(),
                author
        );

        Post savedPost = postRepository.save(post);
        return new PostResponseDto(savedPost);
    }


    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getAllPost() {
        List<Post> posts = postRepository.findAll(); // TODO: 전체 조회

        return posts.stream()
                .map(post -> new PostResponseDto(post))
                .collect(Collectors.toList());
    }

    public PostResponseDto updatePost(Long postId, PostRequestDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
;
        post.changeTitle(request.getTitle());
        post.changeContent(request.getContent());
        post.changeImage(request.getImage());

        return new PostResponseDto(post);
    }


    public void deletePost(Long postId) {
        postRepository.deletePost(postId);
    }


}
