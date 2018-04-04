package org.abigovor.springblog.web.controller;

import org.abigovor.springblog.domain.Post;
import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BlogControllerTest {

    @InjectMocks
    private BlogController controller;

    @Mock
    PostService postService;

    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getPostsByCreator() throws Exception {

        List<Post> postList = new ArrayList<>();

        User creator = new User("tom_lety@gmail.com", "qwe123$");
        creator.setId(1);
        creator.setUsername("Tommy");

        Post post1 = new Post();
        post1.setId(1);
        post1.setTitle("Test post 1");
        post1.setBody("Lorem");
        post1.setUser(creator);
        postList.add((post1));

        Post post2 = new Post();
        post2.setId(2);
        post2.setTitle("Test post 2");
        post2.setBody("Lorem");
        post2.setUser(creator);
        postList.add(post2);

        Page<Post> page = new PageImpl<>(postList);
        when(postService.findByUserName(eq("Tommy"), Matchers.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/post/getByCreator/Tommy")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements", is(postList.size())))
                .andExpect(jsonPath("$.numberOfElements", is(postList.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void getEmptyPostsByCreator() throws Exception {

        List<Post> postList = new ArrayList<>();

        Page<Post> page = new PageImpl<>(postList);
        when(postService.findByUserName(eq("Tommy"), Matchers.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/post/getByCreator/Tommy")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements", is(postList.size())))
                .andExpect(jsonPath("$.numberOfElements", is(postList.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void getPost() throws Exception {

        Post post = new Post();
        post.setId(1);
        post.setTitle("Test post");
        post.setBody("Lorem");

        Optional<Post> optionalPost = Optional.of(post);

        when(postService.findById(1)).thenReturn(optionalPost);

        mockMvc.perform(get("/post/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test post")))
                .andExpect(status().isOk());
    }

    @Test
    public void getNotExistPost() throws Exception {

        when(postService.findById(1)).thenReturn(Optional.<Post>empty());

        mockMvc.perform(get("/post/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllPosts() throws Exception {
        List<Post> postList = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1);
        post1.setTitle("Test post 1");
        post1.setBody("Lorem");
        postList.add(post1);

        Post post2 = new Post();
        post2.setId(2);
        post2.setTitle("Test post 2");
        post2.setBody("Lorem");
        postList.add(post2);

        Page<Post> page = new PageImpl<>(postList);

        when(postService.listAllByPage(Matchers.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/posts"))
                .andExpect(jsonPath("$.totalElements", is(postList.size())))
                .andExpect(jsonPath("$.numberOfElements", is(postList.size())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void createPost() throws Exception {
        Post created = new Post();
        created.setId(1);
        created.setTitle("Post title");

        User creator = new User("creator", "test");
        creator.setId(2);
        created.setUser(creator);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator, creator.getPassword()));

        when(postService.save(Matchers.any(Post.class))).thenReturn(created);

        mockMvc.perform(post("/post")
                .content("{\"title\":\"Post title\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(created.getTitle())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void updateExistingPost() throws Exception {

        Post updated = new Post();
        updated.setId(1);
        updated.setTitle("Update title");

        User creator = new User("creator", "test");
        creator.setId(2);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator, creator.getPassword()));

        when(postService.findByPostIdAndCreatorId(1, 2)).thenReturn(Optional.of(updated));
        when(postService.save(Matchers.any(Post.class))).thenReturn(updated);

        mockMvc.perform(put("/post/1")
                .content("{\"title\":\"Update title\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(updated.getTitle())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void updateNotExistingPost() throws Exception {

        User creator = new User("creator", "test");
        creator.setId(1);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator, creator.getPassword()));

        when(postService.findByPostIdAndCreatorId(1, 1)).thenReturn(Optional.<Post>empty());

        mockMvc.perform(put("/post/1")
                .content("{\"title\":\"Update title\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void deleteExistingPost() throws Exception {

        Post updated = new Post();
        updated.setId(1);
        updated.setTitle("Update title");

        User creator = new User("creator", "test");
        creator.setId(2);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator, creator.getPassword()));

        when(postService.findByPostIdAndCreatorId(1, 2)).thenReturn(Optional.of(updated));

        mockMvc.perform(delete("/post/1"))
                .andExpect(content().string("Post 1 is deleted"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void deleteNotExistingPost() throws Exception {

        User creator = new User("creator", "test");
        creator.setId(1);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(creator, creator.getPassword()));

        when(postService.findByPostIdAndCreatorId(1, 1)).thenReturn(Optional.<Post>empty());

        mockMvc.perform(delete("/post/1"))
                .andExpect(status().isNotFound());
    }
}