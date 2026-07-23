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
class CommentTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Rollback(false)
    void unidirectionalManyToOneTest() {

        User user1 = new User("leehyye@gmail.com",
                "qweasd!@WW",
                "효롤리",
                "https://cute_dog/image.jpg",
                UserRole.USER);

        entityManager.persist(user1);

        User user2 = new User("yiurimo@gmail.com",
                "pass123!S",
                "rachel");

        entityManager.persist(user2);

        entityManager.flush();

        Post post1 = new Post("첫 제목",
                "첫 게시글 내용입니다.",
                user1);
        entityManager.persist(post1);
        entityManager.flush();


        // 댓글 작성하기
        Comment comment1 = new Comment(
                "저의 첫 게시글 많은 관심 부탁드립니다~",
                user1,
                post1
        );

        entityManager.persist(comment1);

        Comment comment2 = new Comment(
                "게시글 잘 봤습니다. 댓글 남겨요~",
                user2,
                post1
        );

        entityManager.persist(comment2);

        entityManager.flush();
    }
}