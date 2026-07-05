package kr.adapterz.jpa_practice.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Embeddable // 다른 엔티티에 끼워 넣는 값 객체
@Getter
@Setter
@EqualsAndHashCode
public class LikeId implements Serializable { //Serializable 의미: JPA(하이버네이트) 규격상 "복합키로 쓸 클래스는 언제든 직렬화(바이트 변환)가 가능하도록 Serializable을 반드시 구현해야 한다"라고 강제하고 있기 때문에 적어둔 것
    private Long postId;
    private Long userId;

    // @NoArgsConstructor의 역할, 어노테이션으로 대체 가능
    public LikeId() {}

    // @AllArgsConstructor의 역할, 어노테이션으로 대체 가능
    public LikeId(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
