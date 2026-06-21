package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.adapterz.jpa_practice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostInfoTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Rollback(false)
    void OneToOneTest() {
        // 유저 저장
        User user = new User(
                "star@naver.com",
                "GHJghj56%",
                "nicecity");
        entityManager.persist(user);
        entityManager.flush();

        // 게시글 저장
        Post post = new Post("두번째 제목",
                "두번쨰 게시글 내용입니다.",
                user);
        entityManager.persist(post);
        entityManager.flush();

        for (int i=0; i<5; i++){
            Comment comment = new Comment(
              "같은 사람이 " + i + "번째 댓글을 달아볼게요.",
              user,
              post
            );
            post.getPostInfo().increaseCommentCount();
            entityManager.persist(comment);
        }
        entityManager.flush();

        Like like = new Like(true, user, post);
        post.getPostInfo().increaseLikeCount();
        entityManager.persist(like);
        entityManager.flush();

        //postInfo 동기화 어카지???

    }
}