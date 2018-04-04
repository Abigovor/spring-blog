package org.abigovor.springblog.service;

import org.abigovor.springblog.domain.Post;
import org.abigovor.springblog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Page<Post> listAllByPage(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Optional<Post> findById(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public Optional<Post> findByPostIdAndCreatorId(Integer postId, Integer creatorId) {
        return postRepository.findByPostIdAndCreatorId(postId, creatorId);
    }

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Page<Post> findByUserName(String creatorName, Pageable pageable) {
        return postRepository.findByUserName(creatorName, pageable);
    }
}
