package org.abigovor.springblog.web.controller;

import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.service.UserService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController controller;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void startMocks() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    public void getAllUsers() throws Exception {
        List<User> userList = new ArrayList<User>();
        userList.add(new User("stan@mail.ru", "stan"));
        userList.add(new User("anna@yandex.ru", "anna"));
        userList.add(new User("tom@gmail.com", "tom"));

        Page<User> page = new PageImpl<>(userList);

        when(userService.listAllByPage(Matchers.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$.totalElements", is(userList.size())))
                .andExpect(jsonPath("$.numberOfElements", is(userList.size())))
                .andExpect(status().isOk());
    }

    @Test
    public void getUser() throws Exception {

        Optional<User> tom = Optional.of(new User("tom@gmail.com", "tom"));
        tom.get().setId(1);

        when(userService.findById(1)).thenReturn(tom);

        mockMvc.perform(get("/user/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is("tom@gmail.com")))
                .andExpect(status().isOk());
    }

    @Test
    public void getNotExistUser() throws Exception {

        when(userService.findById(1)).thenReturn(Optional.<User>empty());

        mockMvc.perform(get("/user/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void updateExistingUser() throws Exception {
        User updated = new User("update email", "test");
        updated.setId(2);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(updated, updated.getPassword()));

        when(userService.save(updated)).thenReturn(updated);

        mockMvc.perform(put("/user")
                .content("{\"email\":\"update email\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(updated.getEmail())))
                .andExpect(status().isOk());
    }
}