package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // TODO: Setter는 직접적으로 사용하지말고 불러올땐 메소드에 의도가 보이도록 합시다.
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.adapterz.jpa_practice.repository.CommentRepository;
import kr.adapterz.jpa_practice.repository.LikeRepository;

import kr.adapterz.jpa_practice.entity.Comment;

@Entity
@Getter @Setter
@SequenceGenerator(
        name = "post_seq",
        sequenceName = "post_seq",
        allocationSize = 30
)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(nullable = false, length = 26)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.PERSIST)
    private PostInfo postInfo;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    // TODO: 동기화 시점-서비스계층
    @Column(nullable = false, length = 10)
    private String nickname; // User랑 똑같은 @Column 적용해줘야하나요?

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;

    protected Post() {}

    // 이미지는 생성자에서 빼고 메소드만 따로 둔다
    public Post(String title, String content, User author) {
        this.nickname = author.getNickname(); //얘는 복사임
        this.author = author;// 외래키인데
        this.title = title;
        this.content = content;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.postInfo = new PostInfo(this); // 여기서 this가 post맞지?
    }


    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void addPostImage(String url) {
        PostImage image = new PostImage(url, this);
        postImages.add(image);
    }


}
