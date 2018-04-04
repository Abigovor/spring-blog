package org.abigovor.springblog.web.controller;

import org.abigovor.springblog.domain.Post;
import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.error.PostNotFoundException;
import org.abigovor.springblog.service.PostService;
import org.abigovor.springblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
public class BlogController {

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @GetMapping(value = "/post/getByCreator/{creatorName}")
    Page<Post> getByCreator(@PathVariable String creatorName, Pageable pageable) throws Exception {
        return postService.findByUserName(creatorName, pageable);
    }

    @GetMapping(value = "/post/get/{id}")
    Post getById(@PathVariable Integer id) throws Exception {

        Optional<Post> post = postService.findById(id);
        if (!post.isPresent())
            throw new PostNotFoundException();

        return post.get();
    }

    @GetMapping(value = "/posts")
    Page<Post> allPost(Pageable pageable) {
        return postService.listAllByPage(pageable);
    }

    @PostMapping(value = "/post")
    Post createPost(@RequestBody Post post) {

        User creator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (post.getCreatedAt() == null)
            post.setCreatedAt(new Date());

        post.setUser(creator);
        return postService.save(post);
    }

    @PutMapping(value = "/post/{id}")
    Post updatePost(@PathVariable int id, @RequestBody Post post) {

        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (post.getUpdatedAt() == null)
            post.setUpdatedAt(new Date());

        Optional<Post> updated = postService.findByPostIdAndCreatorId(id, userDetails.getId());

        if (!updated.isPresent())
            throw new PostNotFoundException();

        post.setId(id);
        post.setUser(updated.get().getUser());

        return postService.save(post);
    }

    @DeleteMapping(value = "/post/{postId}")
    String deletePost(@PathVariable int postId) {

        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Post> postOptional = postService.findByPostIdAndCreatorId(postId, userDetails.getId());

        if (!postOptional.isPresent())
            throw new PostNotFoundException();

        Post deletePost = new Post();
        deletePost.setId(postId);

        postService.deletePost(deletePost);

        return "Post " + postId + " is deleted";
    }

}
