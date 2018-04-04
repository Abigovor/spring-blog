package org.abigovor.springblog.domain;

import lombok.Data;

@Data
public class UserRegistration {
    private String email;
    private String password;
    private String passwordConfirmation;
}
