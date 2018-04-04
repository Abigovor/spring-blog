package org.abigovor.springblog.service;

import org.abigovor.springblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findById(Integer id);

    Page<User> listAllByPage(Pageable pageable);

    Optional<User> findByName(String username);

    Optional<User> findByEmail(String email);
}
