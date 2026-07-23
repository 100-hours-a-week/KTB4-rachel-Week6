package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter; // TODO: Setter는 직접적으로 사용하지말고 불러올땐 메소드에 의도가 보이도록 합시다.

import java.io.Serializable;

@Table(name = "likes")
@Entity
@Getter @Setter
public class Like {

    @EmbeddedId
    @Column(nullable = false)
    private LikeId likeId;

    @Column(nullable = false)
    private boolean isLike = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") //LikeId에 선언한 필드 그대로 써야 함
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")//LikeId에 선언한 필드 그대로 써야 함
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Like() {}

    public Like(boolean isLike, User author, Post post) {
        this.isLike = isLike;
        this.author = author;
        this.post = post;

        this.likeId = new LikeId();
    }

    public void setPost(Post post) {
        this.post = post;

        if(post != null) {
            post.getLikes().add(this);
        }
    }

    public void disconnectPost(Post post) {
        if(post != null) {
            post.getLikes().remove(this);
            this.post = null;
        }
    }
}
