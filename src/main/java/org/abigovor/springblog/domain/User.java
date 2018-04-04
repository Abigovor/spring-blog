package org.abigovor.springblog.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id @GeneratedValue private Integer id;
    private String username;
    private String email;
    private String password;
    private Integer age;
    private String description;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.description = user.getDescription();

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, Integer age, String description) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.age = age;
        this.description = description;
    }
}
