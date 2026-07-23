package kr.adapterz.jpa_practice.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Rollback(false)
    void idTest() {
        User user1 = new User("yiurimo@gmail.com",
                "pass123!S",
                "rachel");

        entityManager.persist(user1);

        User user2 = new User("leehyye@gmail.com",
                "qweasd!@WW",
                "효롤리",
                "https://cute_dog/image.jpg",
                UserRole.USER);

        entityManager.persist(user2);

    }

    // 식별자 생성 전략 sequence가 작동하는지 확인하는 테스트
    @Test
    @Rollback(false)
    void isStrategyTest() {
        for(int i=0; i<35; i++) {
            User user1 = new User(
                    "abcde" + i + "@gmail.com",
                    "qweasd123!D" + i,
                    "junho" + i
            );

            entityManager.persist(user1);
        }

        User user2 = new User(
                "Imageabcde@gmail.com",
                "dhu@!3F",
                "ariangrae",
                "http://cuteSinger.png",
                UserRole.USER
        );

        entityManager.persist(user2);

    }


}