package org.abigovor.springblog;

import org.abigovor.springblog.domain.User;
import org.abigovor.springblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBlogApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;


    public static void main(String[] args) {
        SpringApplication.run(SpringBlogApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        User alex = new User("alex@tr.com", "123");
        userService.save(alex);
    }
}
