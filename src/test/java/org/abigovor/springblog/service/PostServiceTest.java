package org.abigovor.springblog.service;

import org.abigovor.springblog.domain.Post;
import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PostServiceTest {

    private Pageable pageable;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll() {
        List<Post> postList = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1);
        post1.setTitle("post 1 title");
        postList.add(post1);

        Post post2 = new Post();
        post1.setId(2);
        post1.setTitle("post 2 title");
        postList.add(post2);

        pageable = new PageRequest(1, 2);
        Page<Post> page = new PageImpl<>(postList);

        when(postRepository.findAll(pageable)).thenReturn(page);

        Page<Post> result = postService.listAllByPage(pageable);

        assertEquals(2, result.getNumberOfElements());
    }


    @Test
    public void testFindPostById() {
        Post post = new Post();
        post.setId(1);
        post.setTitle("Post field");
        post.setBody("lorem input");


        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        Optional<Post> result = postService.findById(1);
        assertTrue(result.isPresent());
        assertEquals(post.getTitle(), result.get().getTitle());
        assertEquals(post.getId(), result.get().getId());
    }


    @Test
    public void testFindNotExistUserById() {
        when(postRepository.findById(3)).thenReturn(Optional.empty());

        Optional<Post> result = postService.findById(3);
        assertFalse(result.isPresent());
    }


    @Test
    public void testFindByPostIdAndCreatorId() {
        Post post = new Post();
        post.setId(5);
        post.setTitle("Post field");
        post.setBody("lorem input");

        User user = new User("qwe@tr.ua", "222");
        user.setId(1);
        post.setUser(user);


        when(postRepository.findByPostIdAndCreatorId(post.getId(), user.getId())).thenReturn(Optional.of(post));

        Optional<Post> result = postService.findByPostIdAndCreatorId(post.getId(), user.getId());

        assertTrue(result.isPresent());
        assertEquals(post.getTitle(), result.get().getTitle());
        assertEquals(user.getEmail(), result.get().getUser().getEmail());
        assertEquals(post.getId(), result.get().getId());
    }

    @Test
    public void testFindNotExistByPostIdAndCreatorId() {
        when(postRepository.findByPostIdAndCreatorId(3, 3)).thenReturn(Optional.<Post>empty());

        Optional<Post> result = postService.findByPostIdAndCreatorId(3, 3);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByCreatorName() {
        List<Post> postList = new ArrayList<>();
        Post post = new Post();
        post.setId(5);
        post.setTitle("Post field");
        post.setBody("lorem input");

        User user = new User("qwe@tr.ua", "222");
        user.setId(1);
        post.setUser(user);
        postList.add(post);

        pageable = new PageRequest(1, 2);
        Page<Post> page = new PageImpl<>(postList);

        when(postRepository.findByUserName(user.getEmail(), pageable)).thenReturn(page);

        Page<Post> result = postService.findByUserName(user.getEmail(), pageable);

        assertEquals(1, result.getNumberOfElements());
    }

}