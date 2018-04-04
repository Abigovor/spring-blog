package org.abigovor.springblog.service;

import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.repository.UserRepository;
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

public class UserServiceTest {

    private Pageable pageable;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAll() {
        List<User> userList = new ArrayList<User>();
        userList.add(new User("stan@mail.ru", "stan"));
        userList.add(new User("anna@yandex.ru", "anna"));
        userList.add(new User("tom@gmail.com", "tom"));

        pageable = new PageRequest(1, 4);
        Page<User> page = new PageImpl<User>(userList);

        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.listAllByPage(pageable);

        assertEquals(3, result.getNumberOfElements());
    }


    @Test
    public void testFindUserById() {
        User user = new User("stan@mail.ru", "stan");
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1);
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }


    @Test
    public void testFindNotExistUserById() {
        when(userRepository.findById(8)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(8);
        assertFalse(result.isPresent());
    }


    @Test
    public void testFindUserByEmail() {
        User user = new User("stan@mail.ru", "stan");

        when(userRepository.findByEmail("stan@mail.ru")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("stan@mail.ru");
        assertTrue(result.isPresent());
        assertEquals("stan@mail.ru", result.get().getEmail());
    }


    @Test
    public void testFindUserByName() {
        User user = new User("stan@mail.ru", "stan");
        user.setUsername("Stani");

        when(userRepository.findByUsername("Stani")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByName("Stani");
        assertTrue(result.isPresent());
        assertEquals("Stani", result.get().getUsername());
    }


    @Test
    public void saveUser() {
        User addUser = new User("New user", "pswd");
        addUser.setId(8);

        when(userRepository.save(addUser)).thenReturn(addUser);

        User result = userService.save(addUser);

        assertEquals(new Integer(8), result.getId());
        assertEquals("New user", result.getEmail());
        assertEquals("pswd", result.getPassword());
    }
}