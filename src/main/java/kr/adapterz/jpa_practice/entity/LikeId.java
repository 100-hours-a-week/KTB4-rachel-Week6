package kr.adapterz.jpa_practice.entity;


import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;

@Embeddable // 다른 엔티티에 끼워 넣는 값 객체
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Long postId;
    private Long userId;
}
