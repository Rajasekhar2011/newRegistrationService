package com.spring.reactive.registration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }

    public UserNotFoundException(HttpStatus status, String message, Throwable e) {
        super(status, message, e);
    }
}
