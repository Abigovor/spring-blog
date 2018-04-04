package org.abigovor.springblog.web.controller;

import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.domain.UserRegistration;
import org.abigovor.springblog.error.UserNotFoundException;
import org.abigovor.springblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/secret")
    String secret() {
        return "secret";
    }


    @GetMapping(value = "/users")
    Page<User> allUser(Pageable pageable) {
        return userService.listAllByPage(pageable);
    }

    @GetMapping(value = "/user/get/{id}")
    User getById(@PathVariable Integer id) throws Exception {

        Optional<User> user = userService.findById(id);

        if (!user.isPresent())
            throw new UserNotFoundException();

        return user.get();
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegistration userRegistration) {
        if (!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation())) {
            return "Password do not match";
        } else if (userService.findByEmail(userRegistration.getEmail()) != null) {
            return "User already exists";
        }

        User createdUser = userService.save(new User(userRegistration.getEmail(), userRegistration.getPassword()));

        return "User created";
    }

    @PutMapping(value = "/user")
    User updateUser(@RequestBody User user) {

        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getPassword() == null)
            user.setPassword(userDetails.getPassword());

        if (user.getEmail() == null)
            user.setEmail(userDetails.getEmail());

        user.setId(userDetails.getId());
        return userService.save(user);
    }
}
