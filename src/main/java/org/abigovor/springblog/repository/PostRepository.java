package org.abigovor.springblog.repository;

import org.abigovor.springblog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(Integer id);

    @Query("select p from Post p where p.id = ?1 and p.user.id = ?2")
    Optional<Post> findByPostIdAndCreatorId(Integer postId, Integer creatorId);

    @Query("select p from Post p where p.user.username = ?1")
    Page<Post> findByUserName(String creatorName, Pageable pageable);
}
