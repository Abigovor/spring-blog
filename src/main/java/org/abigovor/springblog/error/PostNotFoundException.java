package org.abigovor.springblog.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "POST_IS_NOT_FOUND")
public class PostNotFoundException extends RuntimeException {
}
