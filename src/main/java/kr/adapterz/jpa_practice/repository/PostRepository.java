package kr.adapterz.jpa_practice.repository;

import kr.adapterz.jpa_practice.entity.Post;
import kr.adapterz.jpa_practice.entity.PostImage;
import kr.adapterz.jpa_practice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


}
