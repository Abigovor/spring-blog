package org.abigovor.springblog.service;

import org.abigovor.springblog.domain.CustomUserDetails;
import org.abigovor.springblog.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> optionalUser = userService.findByEmail(email);

        optionalUser
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        return optionalUser.map(CustomUserDetails::new).get();
    }

}