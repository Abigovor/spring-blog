package org.abigovor.springblog.service;

import org.abigovor.springblog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Post save(Post post);

    Page<Post> listAllByPage(Pageable pageable);

    Optional<Post> findById(Integer id);

    Optional<Post> findByPostIdAndCreatorId(Integer postId, Integer creatorId);

    void deletePost(Post post);

    Page<Post> findByUserName(String creatorName, Pageable pageable);
}
