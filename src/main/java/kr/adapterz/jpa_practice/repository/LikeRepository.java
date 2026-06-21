package kr.adapterz.jpa_practice.repository;

import kr.adapterz.jpa_practice.entity.Like;
import kr.adapterz.jpa_practice.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
