package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PostInfo {

    @Id
    @OneToOne(fetch = FetchType.LAZY) // 애초에 PostInfo의 식별자키가 따로 없고 외래키 post_id로써 식별자를 사용하는데 이 식별자 생성전략을 지정해줄 필요가 있나?
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "comment_count", nullable = false)
    private int commentCount;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    protected PostInfo() {}

    public PostInfo(Post post) {
        this.post = post;

         //방법 1
        this.commentCount = post.getComments().size();
        this.likeCount = post.getLikes().size();

        // 방법 2) 아니면 아예 0으로 시작하는게 맞을련지? 위의 방법은 너무 연관관계 때문에 얽힘
//        this.commentCount = 0;
//        this.likeCount = 0;
        // viewCount는 어떻게 감지하지?
    }

    public void increaseCommentCount() {
        commentCount++;
    }

    public void decreaseCommentCount() { commentCount--; }

    public void increaseLikeCount() {
        likeCount++;
    }

    public void decreaseLikeCount() {likeCount--;}
}
