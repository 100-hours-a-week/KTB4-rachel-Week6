package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // TODO: Setter는 직접적으로 사용하지말고 불러올땐 메소드에 의도가 보이도록 합시다.

import java.time.LocalDateTime;


@Entity
@Getter @Setter
@SequenceGenerator(
        name = "comment_seq",
        sequenceName = "comment_seq",
        allocationSize = 5
)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false, length = 10)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY) // nickname 때문에 User 엔티티를 불러오는거지만 comment_id로 이미 식별자를 가지고 있는데 굳이 user_id가 필요하긴할까? nickname을 그저 복사하기용으로 필요한거겠지?
    @JoinColumn(name = "user_id", nullable = false) // DB FK 연결
    private User author;

    // 내가 설계한 ERD에는 post에 대한 것을 안넣었다. commentId만으로도 post를 구별할 수 있다 생각해서
    // 그 말은 즉, Comment 엔티티에 어떤 post인지는 생성자에 명시해줘야 한다는 얘기겠지요? 식별자가 복합키(post_id + comment_id)가 아니라 comment_id이고, 이렇게 하려면 어떤 Post인지는 생성자에 맞춰줘야죠?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Comment() {}

    public Comment(String commentContent, User author, Post post) {
        this.author = author;
        this.nickname = author.getNickname();
        this.post = post;
        this.commentContent = commentContent;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeContent(String commentContent) {
        this.commentContent = commentContent;
    }

}
