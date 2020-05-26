package io.aktivator.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotRegisteredException extends RuntimeException {

    public UserNotRegisteredException(String message) {
        super(message);
    }
}
